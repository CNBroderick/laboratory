package org.bklab.vaadin.domain;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DomainIsAvailable {
    static int count = 0, index = 0;
    public boolean domainIsAvailable(String domain){
        boolean isAvailable = false;  //该域名是否可用
        try {
            URL url = new URL("http://panda.www.net.cn/cgi-bin/check.cgi?area_domain=" + domain);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);  //毫秒
            connection.setReadTimeout(5000);

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";  //每次读取一行数据
            String reg = "<original>(.*?)</original>";  //正则
            while((line = reader.readLine()) != null){
                if(line.matches(reg)){
//                  System.out.println(line);
                    //只有两种状态，210表示可用，211表示不可用
                    String state = line.substring(10, 13);
                    if("210".equals(state))
                        isAvailable = true;
                }
            }

        }  catch (IOException e) {
            e.printStackTrace();
            System.out.println(domain + " not have result");
        }
        return isAvailable;
    }

    public void startQuery(char c1, char c2) {
        StringBuilder domain = new StringBuilder();
        FileWriter writer;
        FileWriter log;
        String suffix = "labs.com";
        try {
            writer = new FileWriter("E:\\available domain t.txt", true);
            log = new FileWriter("E:\\available domain t.log", false);
            long startTime = new Date().getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            writer.append("开始查询：" + sdf.format(startTime)).append("\r\n");
            writer.flush();
            for (int i = c1; i <= '9'; i++) {
                for (int j = c2; j <= '9'; j++) {
                            domain.append(Character.toChars(i));
                            domain.append(Character.toChars(j));
                            domain.append(suffix);
                            if (domainIsAvailable(domain.toString())) {
                                System.out.println(domain.toString() + " 未被注册");
                                writer.append(domain.toString());
                                writer.append("\r\n");
                                ++count;
                                writer.flush();
                                log.flush();
                            } else {
                                log.append(String.format("%6d.\t%12ds\t%s", index, (new Date().getTime() - startTime) / 1000, domain.toString() + " not available.")).append("\r\n");
                            }
                            domain.delete(0, domain.length());
                            ++index;

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

    public void MultiWork(int n) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(n, 8, 100, TimeUnit.HOURS, new ArrayBlockingQueue(8));
        executor.execute(() -> { });
    }

    public static void main(String[] args) {
        if(new DomainIsAvailable().domainIsAvailable("062.me")){
            System.out.println("ok");
        } else {
            System.out.println("false");
        }
    }

}
