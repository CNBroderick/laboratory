package main;

import org.bklab.vaadin.domain.DomainIsAvailable;
import org.junit.Test;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DomainTest {
    @Test
    public void checkDomainAvailable() {
        DomainIsAvailable isAvailable = new DomainIsAvailable();
        List<String> availableList = new ArrayList<>();
        String prefix = "bk";
        String suffix = ".com";
        String domain = "";
        for (int i = 10; i < 1000; i++) {
            domain = prefix + i + suffix;
            if (isAvailable.domainIsAvailable(domain)) {
                System.out.println("域名：" + domain + "未被注册 ---------------------");
                availableList.add(domain);
            } else {
//                System.out.println("域名：" + domain + "已被注册。" );
            }
        }
//        if (availableList.size() > 0) {
//            System.out.println("可用域名有：");
//            for (int i = 0; i < availableList.size(); i++) {
//                System.out.print(availableList.get(i) + "\t");
//                if((i + 1)% 8 == 0) { System.out.println();}
//            }
//        }

    }

    @Test
    public void asciiTest() {
        StringBuilder domain = new StringBuilder();
        DomainIsAvailable is = new DomainIsAvailable();
        FileWriter writer;
        FileWriter log;
        String suffix = ".com";
        try {
            writer = new FileWriter("E:\\available domain t.txt", true);
            log = new FileWriter("E:\\available domain t.log", true);
            long startTime = new Date().getTime();
            int count = 0, index = 0;
            int maxCount =(int) Math.pow(26, 4);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            writer.append("开始查询：" + sdf.format(startTime)).append("\r\n");
            for (int i = 'a'; i <= 'z'; i++) {
                for (int j = 'a'; j <= 'z'; j++) {
                    for (int k = 'a'; k <= 'z'; k++) {
                        for (int l = 'a'; l <= 'z'; l++) {
                            domain.append(Character.toChars(i));
                            domain.append(Character.toChars(j));
                            domain.append(Character.toChars(k));
                            domain.append(Character.toChars(l));
                            domain.append(suffix);
                            if (is.domainIsAvailable(domain.toString())) {
                                writer.append(domain.toString());
                                writer.append("\r\n");
                                ++count;
                            }
                            domain.delete(0, domain.length());
                            ++index;
                        }
                    }
                    log.append(index * 1.0 / maxCount * 100 + "%\t" + ((new Date().getTime() - startTime) / 1000 / 60) + "min").append("\r\n");
                    writer.flush();
                    log.flush();
                }
            }
            writer.append("查询完毕！用时时间：" + (new Date().getTime() - startTime) / 1000 / 60 + " 分。").append("\r\n");
            writer.append("查询完毕！结束时间：" + sdf.format(new Date())).append("\r\n");
            writer.append("共计找到：" + count + "个可用域名。").append("\r\n");
            writer.flush();
            log.flush();
        } catch (Exception ignore) {
        }
    }

    @Test
    public void test() {
        new DomainIsAvailable().MultiWork(8);
    }
}
