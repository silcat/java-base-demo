//package zookeeper.IDGeneger;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//
//@Slf4j
//public class test {
//
//    @Test
//    public void testMakeId() {
//        IDMaker idMaker = new IDMaker();
//        idMaker.init();
//        String nodeName = "/test/IDMaker/ID-";
//        for (int i = 0; i < 10; i++) {
//            String id = idMaker.makeId(nodeName);
//            log.info("第" + i + "个创建的id为:" + id);
//        }
//        idMaker.destroy();
//    }
//    @Test
//    public void testSnowId() {
//        IDMaker idMaker = new IDMaker();
//        idMaker.init();
//        String nodeName = "/test/IDMaker/ID-";
//        for (int i = 0; i < 10; i++) {
//            String id = idMaker.makeId(nodeName);
//            log.info("第" + i + "个创建的id为:" + id);
//        }
//        idMaker.destroy();
//    }
//}
