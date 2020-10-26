package com.changou.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;

import java.util.List;

@CanalEventListener
public class CanalDataListener {
    /**
     * 监控插入数据
     * @param eventType
     * @param rowData
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType,CanalEntry.RowData rowData){

        List<CanalEntry.Column> columnsList = rowData.getAfterColumnsList();
        for(CanalEntry.Column column : columnsList){
            System.out.println("列名"+ column.getName() +"列值："+ column.getValue());
        }
    }

    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EventType eventType,
                              CanalEntry.RowData rowData){
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for(CanalEntry.Column column : beforeColumnsList){
            System.out.println("列名"+ column.getName() +"修改前列值："+ column.getValue());
        }
        for(CanalEntry.Column column : afterColumnsList){
            System.out.println("列名"+ column.getName() +"修改后列值："+ column.getValue());
        }


    }
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType,
                              CanalEntry.RowData rowData){
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        for(CanalEntry.Column column : beforeColumnsList){
            System.out.println(column.getName() + "：" + column.getValue());

        }

    }
    @ListenPoint(eventType = {CanalEntry.EventType.UPDATE,
            CanalEntry.EventType.DELETE,
            CanalEntry.EventType.INSERT},
        schema = {"changgou_content"},
            table = {"tb_content"},
            destination = "example"
    )
    public void onEventCustom(CanalEntry.EventType eventType,
                              CanalEntry.RowData rowData){
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for(CanalEntry.Column column : beforeColumnsList){
            System.out.println("列名 -->"+ column.getName() +"修改前列值："+ column.getValue());
        }
        for(CanalEntry.Column column : afterColumnsList){
            System.out.println("列名 -->"+ column.getName() +"修改后列值："+ column.getValue());
        }

    }
}
