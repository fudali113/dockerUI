package ren.doob.util.dockerapi;

import com.squareup.okhttp.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fudali
 * @package ren.doob.util.dockerapi
 * @class DockerApi
 * @date 2016-2-2
 * <p>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓    Code
 * 　　┏┛┻━━━┛┻┓  is
 * 　　┃　　　　　　　┃  far
 * 　　┃　　　━　　　┃  away
 * 　　┃　┳┛　┗┳　┃  from
 * 　　┃　　　　　　　┃  bug
 * 　　┃　　　┻　　　┃  with
 * 　　┃　　　　　　　┃  the
 * 　　┗━┓　　　┏━┛  animal
 * 　　　　┃　　　┃      protecting
 * 　　　　┃　　　┃神兽保佑,代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * ━━━━━━感觉萌萌哒━━━━━━
 */
@Service
public class DockerApiUtil {

    private final OkHttpClient client = new OkHttpClient();

    /**
     * 方便产生OkHttp Request
     * @param dockerApi api字符串
     * @return OkHttp Request对象
     */
    public Request getOkHttpRequest(String dockerApi){
        return  new Request.Builder()
                .url("http://139.129.4.187:13131/"+ dockerApi)
                .build();
    }

    /**
     * 根据api执行GET请求以查询结果
     * @param dockerApi api 字符串
     * @return json格式字符串结果
     * @throws IOException
     */
    public String getDockerApiJson(String dockerApi) throws IOException {
        Request request = getOkHttpRequest(dockerApi);
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().string();
    }

    public Map postDockerApi(String dockerApi) throws IOException {

        RequestBody formBody = new FormEncodingBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://139.129.4.187:13131/"+ dockerApi)
                .post(formBody)
                .build();


        return notGetRequestTotalMethod(request,dockerApi);
    }

    public Map deleteDockerApi(String dockerApi) throws IOException {

        RequestBody formBody = new FormEncodingBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://139.129.4.187:13131/"+ dockerApi)
                .delete(formBody)
                .build();

        return notGetRequestTotalMethod(request,dockerApi);
    }

    private Map notGetRequestTotalMethod(Request request ,  String dockerApi) throws IOException {
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        HashMap<String , String> map = new HashMap<String , String>();
        map.put("statusCode" , response.code()+"");
        map.put("responseBody" , response.body().string());
        return map;
    }

}
