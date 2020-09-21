package entity;

/**
 * 结果返回工具类
 */
public class ResultUtil {
    /**
     * 添加数据
     * @return
     */
    public static Result addSuccess(){
        return new Result(true,StatusCode.OK,"添加成功");
    }
    public static Result updateSuccess(){

        return new Result(true,StatusCode.OK,"修改成功");
    }
    public static Result updateSuccess(String msg){

        return new Result(true,StatusCode.OK,msg);
    }
    public static Result deleteSuccess(){
        return new Result(true,StatusCode.OK,"删除成功");
    }

    public static <T> Result findSuccess(T data){
        return new Result(true,StatusCode.OK,"查询成功",data);
    }
}
