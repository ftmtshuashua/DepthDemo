package complie;

import com.depth.ap_annoation.Api;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/4/15 13:48
 * </pre>
 */
@Api(method = "DemoMathodName")
public class DemoApi {
    public DemoApi() { //调用Make Project开始生成
        DemoApiGenerate.DemoMathodName();
    }
}
