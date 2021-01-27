package com.tazh.tazhinterface;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TazhInterfaceApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TazhInterfaceApplicationTests {

    /*@Test
    void contextLoads() {
    }*/
   /* @Test
    public void getAllGoodsByStatus(){
        int status = 1;
        System.out.println("进入了单元测试"+goodsInfoMapper.getAllGoodsByStatus(1));
    }*/

}
