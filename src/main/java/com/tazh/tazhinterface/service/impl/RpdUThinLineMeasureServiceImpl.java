package com.tazh.tazhinterface.service.impl;

import com.tazh.tazhinterface.mapper.RpdUThinLineMeasureMapper;
import com.tazh.tazhinterface.pojo.RPD_U_THIN_LINE_MERSURE_T;
import com.tazh.tazhinterface.service.RpdUThinLineMeasureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RpdUThinLineMeasureServiceImpl implements RpdUThinLineMeasureService {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    private RpdUThinLineMeasureMapper rpdUThinLineMeasureMapper;

    @Override
    public Collection<Object[]> getMeasureDataByTime(Date searchData) throws ParseException {
        String[] times = getBeforeAndAfterTime(searchData);
        Date judgeTime = simpleDateFormat.parse("2020-09-01");
        List<RPD_U_THIN_LINE_MERSURE_T> rutlms = new ArrayList<>();
        Timestamp judgeTs = Timestamp.valueOf("2020-08-31 19:00:00");
        //调用查询语句查询储所有的数据
        //根据传入的日期判断用哪一个查询方法
        if(judgeTime.getTime() == searchData.getTime()){
            //查询出所有的数据 单独做逻辑处理
            System.out.println("=========="+"对象查询开始");
            rutlms = rpdUThinLineMeasureMapper.getMeasureDataAllByTime(times[0],times[1]);
            System.out.println("============="+"对象查询结束");
            List<RPD_U_THIN_LINE_MERSURE_T> rutlmsBefore = new ArrayList<>();
            List<RPD_U_THIN_LINE_MERSURE_T> rutlmsAfter = new ArrayList<>();
            //循环遍历这个对象 找到8月31号19点
            for (RPD_U_THIN_LINE_MERSURE_T rutlm:rutlms) {
                if(rutlm.getAcquisition_time().getTime()<=judgeTs.getTime()){
                    rutlmsBefore.add(rutlm);
                }else{
                    rutlmsAfter.add(rutlm);
                }

            }
            //对rutlmsBefore进行遍历计算
            for(int i = 0;i <rutlmsBefore.size();i++){
                RPD_U_THIN_LINE_MERSURE_T rutlm = rutlmsBefore.get(i);
                rutlm.setOl_hh_102(rutlm.getOl_hh_102()*rutlm.getYmd_xyfx2()); //夏子街油量
                rutlm.setOl_hh_101(rutlm.getOl_hh_101()*rutlm.getYmd_xyfx()); //乌尔禾油量
                rutlmsBefore.set(i,rutlm);
            }
            for(int i = 0;i <rutlmsAfter.size();i++){
                RPD_U_THIN_LINE_MERSURE_T rutlm = rutlmsAfter.get(i);
                rutlm.setOl_hh_102(rutlm.getOl_hh_102()*rutlm.getYmd_xyfx2());
                rutlm.setOl_hh_101(rutlm.getOl_hh_1021()*rutlm.getYmd_xyfx()+rutlm.getOl_hh_1022()*rutlm.getYmd_xyfx());
                rutlm.setOl_hh_1021(rutlm.getOl_hh_1021()*rutlm.getYmd_xyfx());
                rutlm.setOl_hh_1022(rutlm.getOl_hh_1022()*rutlm.getYmd_xyfx());

                rutlmsAfter.set(i,rutlm);
            }
            rutlms.clear();
            rutlms.addAll(rutlmsBefore);
            rutlms.addAll(rutlmsAfter);
        }else if(judgeTime.getTime() > searchData.getTime()){
            rutlms = rpdUThinLineMeasureMapper.getMeasureDataByTime(times[0],times[1]);
        }else{
            rutlms = rpdUThinLineMeasureMapper.getMeasureDataByTimeAddCol(times[0],times[1]);
        }
        //将数据转换成List<String[]>
        Collection<Object[]> arrayRutlms = new ArrayList<>();
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        for (int i = 0; i < rutlms.size(); i++) {
            Object[]  a = new Object[20];
            //时间转换成字符串
            a[0] = rutlms.get(i).getAcquisition_time().toString();
            a[1] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getFt_hh_102()));
            //夏子街来液累计流量
            a[2] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getFt_ave2()));
            //夏子街来液含水率
            a[3] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getAt_ave2()));
            //夏子街来液油量
            a[4] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getOl_hh_102()));
            //乌尔禾来液瞬时
            a[5] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getFt_hh_101()));
            //乌尔禾来液累计流量
            a[6] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getFt_ave1()));
            //乌尔禾来液含水率
            a[7] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getAt_ave1()));
            //乌尔禾来液油量
            a[8] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getOl_hh_101()));
            //乌尔禾1线瞬时
            a[9] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getFt_hh_103()));
            //乌尔禾1线累计流量
            a[10] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getFt_ave3()));
            //乌尔禾1线含水率
            a[11] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getAt_ave3()));
            //乌尔禾1线油量
            a[12] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getOl_hh_1021()));
            //乌尔禾2线瞬时
            a[13] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getFt_hh_104()));
            //乌尔禾2线累计流量
            a[14] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getFt_ave4()));
            //乌尔禾2线含水率
            a[15] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getAt_ave4()));
            //乌尔禾2线油量
            a[16] = Double.parseDouble(decimalFormat.format(rutlms.get(i).getOl_hh_1022()));

            arrayRutlms.add(a);
        }
        return arrayRutlms;
    }
    //公有方法 根据现在data返回前天8点的字符串和今天8点的字符串
    public String[] getBeforeAndAfterTime(Date searchData){
        String[] times = null;
        Calendar ca = Calendar.getInstance();
        ca.setTime(searchData);
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date beforeDate = ca.getTime();
        //将两个时间格式化后拼接成字符串
        times = new String[]{simpleDateFormat.format(beforeDate) + " 08:00:00", simpleDateFormat.format(searchData) + " 08:00:00"};
        return  times;
    }
}
