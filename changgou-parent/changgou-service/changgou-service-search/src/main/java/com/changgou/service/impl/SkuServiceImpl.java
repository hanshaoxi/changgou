package com.changgou.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.mapper.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.service.SkuService;
import entity.Result;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Override
    public void importSku() {
        Result<List<Sku>> skuList = skuFeign.findSkuList();
        List<SkuInfo> skuInfoList = JSON.parseArray(JSON.toJSONString(skuList.getData()), SkuInfo.class);
        //List<SkuInfo> skuInfos = new ArrayList<>();
        for (SkuInfo skuInfo : skuInfoList) {
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
            // skuInfos.add(skuInfo);
        }
        skuEsMapper.saveAll(skuInfoList);

    }

    @Override
    public Map<String, Object> search(Map<String, String> queryMap) {

        /**高亮搜索*/


        NativeSearchQueryBuilder query = buildBasicQuery(queryMap);
        HighlightBuilder.Field field = new HighlightBuilder.Field("name");
        field.preTags("<em style='color:red'>");
        field.postTags("</em>");
        field.fragmentOffset(100);
        query.withHighlightFields(field);

        //  AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(query.build(), SkuInfo.class);
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(query.build(), SkuInfo.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                List<T> content = new ArrayList<T>();
                if (hits == null || searchResponse.getHits().getTotalHits() <= 0) {
                    return new AggregatedPageImpl<T>(content);
                }

                for (SearchHit hit : hits.getHits()) {
                    SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    HighlightField highLightFields = highlightFields.get("name");
                    if (highLightFields != null) {
                        StringBuffer buffer = new StringBuffer();
                        for (Text field : highLightFields.getFragments()) {
                            buffer.append(field.toString());
                        }
                        skuInfo.setName(buffer.toString());
                    }
                    content.add((T) skuInfo);

                }
                return new AggregatedPageImpl<T>(content, pageable, searchResponse.getHits().getTotalHits());

            }
        });


        List<SkuInfo> skuInfoList = skuInfos.getContent();
        long rows = skuInfos.getTotalElements();
        int totalPages = skuInfos.getTotalPages();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", rows);
        resultMap.put("skuInfoList", skuInfoList);
        resultMap.put("totalPages", totalPages);
       /* if (queryMap != null && StringUtils.isEmpty(queryMap.get("category"))) {

            resultMap.put("categoryList", searchCategoryList(query));
        }
        if (queryMap != null && StringUtils.isEmpty(queryMap.get("brand"))) {

            resultMap.put("brandList", searchBrandList(query));
        }
        resultMap.put("specList", searchSpecList(query));*/
       Map<String,Object> rtMap = searchGroupList(query,queryMap);
       resultMap.putAll(rtMap);
       return resultMap;
    }

    private NativeSearchQueryBuilder buildBasicQuery(Map<String, String> queryMap) {
        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (queryMap != null && queryMap.size() > 0) {
            String keyworkds = queryMap.get("keywords");
            if (!StringUtils.isEmpty(keyworkds)) {

                boolQueryBuilder.must(QueryBuilders.queryStringQuery(keyworkds).field("name"));
            }
            // query.withQuery(QueryBuilders.queryStringQuery(keyworkds).field("name"));
        }
        if (queryMap != null && !StringUtils.isEmpty(queryMap.get("category"))) {
            boolQueryBuilder.must(QueryBuilders.termQuery("categoryName", queryMap.get("category")));
        }
        if (queryMap != null && !StringUtils.isEmpty(queryMap.get("brand"))) {
            boolQueryBuilder.must(QueryBuilders.termQuery("brandName", queryMap.get("brand")));
        }
        if (queryMap != null && queryMap.size() > 0) {
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.startsWith("spec_")) {
                    boolQueryBuilder.must(QueryBuilders.termQuery("specMap." + key.substring(5), value));
                }
            }
        }
        //价格区间
        if (queryMap != null && !StringUtils.isEmpty(queryMap.get("price"))) {
            String price = queryMap.get("price");
            //去除，"元",以上
            price = price.replace("元", "").replace("以上", "");
            String[] prices = price.split("-");
            if (prices != null && prices.length > 0) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gt(Integer.parseInt(prices[0])));
                if (prices.length == 2) {
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(Integer.parseInt(prices[1])));
                }
            }

        }
        //分页
        Integer pagNum = convertPageNum(queryMap);
        Integer size = 10;
        query.withPageable(PageRequest.of(pagNum - 1, size));
        query.withQuery(boolQueryBuilder);
        //排序
        String sortField = queryMap.get("sortField");
        String sortRule = queryMap.get("sortRule");
        if (!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(sortRule)) {
            query.withSort(new FieldSortBuilder(sortField).order(SortOrder.valueOf(sortRule)));
        }
        return query;
    }

    private Integer convertPageNum(Map<String, String> searchMap) {
        try {
            if (searchMap != null && !StringUtils.isEmpty(searchMap.get("pageNum"))) {
                return Integer.parseInt(searchMap.get("pageNum"));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 类别分组查询
     *
     * @param query
     * @return
     */
    private List<String> searchCategoryList(NativeSearchQueryBuilder query) {
        query.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(query.build(), SkuInfo.class);
        StringTerms skuCategory = (StringTerms) aggregatedPage.getAggregation("skuCategory");
        List<StringTerms.Bucket> buckets = skuCategory.getBuckets();
        List<String> categoryName = new ArrayList();
        for (StringTerms.Bucket bucket : buckets) {
            String category = bucket.getKeyAsString();
            categoryName.add(category);
        }
        return categoryName;
    }


    private Map<String,Object> searchGroupList(NativeSearchQueryBuilder query, Map<String, String> searchMap) {

        Map<String,Object> resultMap = new HashMap<>();
       /* if (searchMap != null && !StringUtils.isEmpty(searchMap.get("category"))) {
*/
            query.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
       /* }*/
        /*if (searchMap != null && !StringUtils.isEmpty("brand")) {
*/
            query.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
       /* }*/
        query.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword"));


        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(query.build(), SkuInfo.class);
       /* if (searchMap != null && !StringUtils.isEmpty(searchMap.get("category"))) {*/
            StringTerms skuCategory = (StringTerms) aggregatedPage.getAggregation("skuCategory");
            List<String> skuCategoryList = getGroupList(skuCategory);
            resultMap.put("skuCategoryList",skuCategoryList);
        /*}*/
       /* if (searchMap != null && !StringUtils.isEmpty("brand")) {*/
        StringTerms skuBrand = (StringTerms) aggregatedPage.getAggregation("skuBrand");
            List<String> skuBrandList = getGroupList(skuBrand);
            resultMap.put("skuBrandList",skuBrandList);
        /*}*/
        StringTerms skuSpec = (StringTerms) aggregatedPage.getAggregation("skuSpec");
        List<String> skuSpecList = getGroupList(skuSpec);
        Map<String,Set<String>> specSetMap = getSpecSetMap(skuSpecList);
        resultMap.put("specSetMap",specSetMap);


        return resultMap;
    }
        private List<String> getGroupList(StringTerms stringTerms){
            List<String> groupNameList = new ArrayList();
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                String field = bucket.getKeyAsString();
                groupNameList.add(field);
            }
            return groupNameList;
        }
    /**
     * 品牌分组查询
     *
     * @param query
     * @return
     */
    private List<String> searchBrandList(NativeSearchQueryBuilder query) {
        query.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(query.build(), SkuInfo.class);
        StringTerms skuBrand = (StringTerms) aggregatedPage.getAggregation("skuBrand");
        List<StringTerms.Bucket> buckets = skuBrand.getBuckets();
        List<String> brandName = new ArrayList();
        for (StringTerms.Bucket bucket : buckets) {
            String brand = bucket.getKeyAsString();
            brandName.add(brand);
        }
        return brandName;
    }

    private Map<String, Set<String>> searchSpecList(NativeSearchQueryBuilder builder) {
        builder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword").size(10000));
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        StringTerms skuSpec = (StringTerms) skuInfos.getAggregation("skuSpec");
        List<String> specList = new ArrayList<>();
        for (StringTerms.Bucket bucket : skuSpec.getBuckets()) {
            String spec = bucket.getKeyAsString();
            specList.add(spec);
        }
        return getSpecSetMap(specList);

    }

    private Map<String, Set<String>> getSpecSetMap(List<String> specList) {
        Map<String, Set<String>> allMap = new HashMap<>();
        //1.遍历生成对应map
        for (String specStr : specList) {
            Map<String, String> specMap = JSON.parseObject(specStr, Map.class);
            for (Map.Entry<String, String> entry : specMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Set<String> specSet = allMap.get(key);
                if (specSet == null) {
                    specSet = new HashSet<>();
                }
                specSet.add(value);
                allMap.put(key, specSet);
            }
        }
        return allMap;
    }
}
