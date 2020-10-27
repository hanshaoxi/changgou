package com.changgou.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.mapper.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.service.SkuService;
import entity.Result;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

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
        for (SkuInfo skuInfo : skuInfoList){
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
           // skuInfos.add(skuInfo);
        }
        skuEsMapper.saveAll(skuInfoList);

    }

    @Override
    public Map<String, Object> search(Map<String,String> queryMap) {
        NativeSearchQueryBuilder query = buildBasicQuery(queryMap);

        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(query.build(), SkuInfo.class);

        List<String> categoryList = searchCategoryList(query);
        List<SkuInfo> skuInfoList = skuInfos.getContent();
        long rows = skuInfos.getTotalElements();
        int totalPages = skuInfos.getTotalPages();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("rows",rows);
        resultMap.put("skuInfoList",skuInfoList);
        resultMap.put("totalPages",totalPages);
        resultMap.put("categoryList",categoryList);
        resultMap.put("brandList",searchBrandList(query));
        resultMap.put("specList",searchSpecList(query));
        return resultMap;
    }

    private NativeSearchQueryBuilder buildBasicQuery(Map<String, String> queryMap) {
        NativeSearchQueryBuilder query = new NativeSearchQueryBuilder();

        if(queryMap!=null && queryMap.size()>0){
           String keyworkds =  queryMap.get("keywords");
           query.withQuery(QueryBuilders.queryStringQuery(keyworkds).field("name"));
        }
        return query;
    }

    /**
     * 类别分组查询
     * @param query
     * @return
     */
    private List<String> searchCategoryList(NativeSearchQueryBuilder query) {
        query.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(query.build(), SkuInfo.class);
        StringTerms skuCategory = (StringTerms) aggregatedPage.getAggregation("skuCategory");
        List<StringTerms.Bucket> buckets = skuCategory.getBuckets();
        List<String> categoryName = new ArrayList();
        for(StringTerms.Bucket bucket : buckets){
            String category = bucket.getKeyAsString();
            categoryName.add(category);
        }
        return categoryName;
    }
    /**
     * 品牌分组查询
     * @param query
     * @return
     */
    private List<String> searchBrandList(NativeSearchQueryBuilder query) {
        query.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(query.build(), SkuInfo.class);
        StringTerms skuBrand = (StringTerms) aggregatedPage.getAggregation("skuBrand");
        List<StringTerms.Bucket> buckets = skuBrand.getBuckets();
        List<String> brandName = new ArrayList();
        for(StringTerms.Bucket bucket : buckets){
            String brand = bucket.getKeyAsString();
            brandName.add(brand);
        }
        return brandName;
    }

    private Map<String,Set<String>> searchSpecList(NativeSearchQueryBuilder builder){
        builder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword").size(10000));
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        StringTerms skuSpec = (StringTerms) skuInfos.getAggregation("skuSpec");
        List<String> specList = new ArrayList<>();
        for(StringTerms.Bucket bucket : skuSpec.getBuckets()){
           String spec =  bucket.getKeyAsString();
           specList.add(spec);
        }
        Map<String, Set<String>> allMap = new HashMap<>();
        //1.遍历生成对应map
        for(String specStr : specList){
            Map<String , String> specMap = JSON.parseObject(specStr,Map.class);
            for(Map.Entry<String,String> entry : specMap.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                Set<String> specSet = allMap.get(key);
                if(specSet == null){
                    specSet = new HashSet<>();
                }
                specSet.add(value);
                allMap.put(key,specSet);
            }
        }
        return allMap;

    }
}
