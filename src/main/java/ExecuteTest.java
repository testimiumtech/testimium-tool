import com.testimium.tool.base.BaseTestCase;
import com.testimium.tool.exception.TaskNotFoundException;
import com.testimium.tool.logging.LogUtil;


public class ExecuteTest extends BaseTestCase {

    public void test() throws TaskNotFoundException {
        beforeTest();
        LogUtil.logToolMsg("Tool started running.....");
        beforeMethod(null,null, null);
        afterTest();
        LogUtil.logToolMsg("Tool stopped running.....");
    }

    public static void main(String[] args) {
           try {
               new ExecuteTest().test();
           } catch (Exception ex) {
               LogUtil.logToolErrorMsg("Exception: ", ex);
               ex.printStackTrace();
           }
    }
}


