package stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2021/2/4.
 */
public class StockStudy {

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    //这个类不能实例化
    private StockStudy(){
    }
    /**
     * 提供精确的加法运算。
     * @paramv1 被加数
     * @paramv2 加数
     *@return 两个参数的和
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /*
     * 提供精确的减法运算。
     * @paramv1 被减数
     * @paramv2 减数
     *@return 两个参数的差
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return  b1.subtract(b2).doubleValue();
    }
    /**
     * 提供精确的乘法运算。
     * @paramv1 被乘数
     * @paramv2 乘数
     *@return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     * @paramv1 被除数
     * @paramv2 除数
     *@return 两个参数的商
     */
    public static double div(double v1,double v2){
        return div(v1,v2,DEF_DIV_SCALE);
    }
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @paramv1 被除数
     * @paramv2 除数
     * @paramscale 表示表示需要精确到小数点以后几位。
     *@return 两个参数的商
     */
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 提供精确的小数位四舍五入处理。
     * @paramv 需要四舍五入的数字
     *@return 四舍五入后的结果
     */
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算回撤比例
     * @param bottom 底
     * @param peak   顶
     * @param retrace 回撤
     * @return
     */
    public static double retraceRatio(double bottom,double peak,double retrace){
        double up = sub(peak,bottom);
        double back = sub(retrace,bottom);
        return div(back,up,3);
    }

    /**
     * 计算当前回撤比例目标值
     * @param bottom 底
     * @param peak   顶
     * @param backRatio 回撤比例
     * @return
     */
    public  static double meanPrice(double bottom,double peak,double backRatio){
        double up = sub(peak,bottom);
        double meanBack = mul(up,backRatio);
        return add(bottom,meanBack);

    }

    /**
     * 计算各黄金点价位
     * @param bottom
     * @param peak
     * @return
     */
    private static final double A = 0.191;
    private static final double B = 0.382;
    private static final double C = 0.500;
    private static final double D = 0.618;
    private static final double E = 0.809;
    private static final double F = 1.382;
    public static List<Double> goldenPrice(double bottom, double peak){
        List<Double> goldenArr = new ArrayList<Double>();
        double up = sub(peak,bottom);
        goldenArr.add(add(bottom,up*A));
        goldenArr.add(add(bottom,up*B));
        goldenArr.add(add(bottom,up*C));
        goldenArr.add(add(bottom,up*D));
        goldenArr.add(add(bottom,up*E));
        goldenArr.add(add(bottom,up*F));
        return goldenArr;
    }

    /**
     * 计算两条节奏线
     * @param top1 顶1
     * @param top2 顶2
     * @param top3 顶3
     * @param bottom1 低1
     * @param bottom2 低2
     * @param bottom3 低3
     * @param direction 上下
     * @return
     */
    public static List<Double> jieZouXian(double top1,double top2,double top3,double bottom1,double bottom2,double bottom3,String direction){
        List<Double> jieZouList = new ArrayList<Double>();
        double ratio1 =0.00;
        double ratio2 =0.00;
        double jieZouOne = 0.00;
        double jieZouTwo = 0.00;
        if(direction != null && "up".equals(direction)){
            ratio1 = retraceRatio(bottom3,top3,bottom2);
            ratio2 = retraceRatio(bottom3,top2,bottom1);
            jieZouOne = add(bottom3,div(sub(top1,bottom3),ratio1));
            jieZouTwo = add(bottom3,div(sub(top1,bottom3),ratio2));
        }else {
            ratio1 = retraceRatio(bottom3, top3, bottom2);
            ratio2 = retraceRatio(bottom3, top2, bottom1);
            jieZouOne = add(bottom3, div(sub(top1, bottom3), ratio1));
            jieZouTwo = add(bottom3, div(sub(top1, bottom3), ratio2));
        }
        jieZouList.add(jieZouOne);
        jieZouList.add(jieZouTwo);
        return jieZouList;
    }

    /**
     * 计算向上节奏线
     * @param A
     * @param B
     * @param C
     * @param D
     * @return
     */
    public static double upJieZouXian(double A,double B,double C,double D){
        return add(A,mul(div(sub(C,A),sub(B,A)),sub(D,A)));
    }

    /**
     * 计算向下节奏线
     * @param A
     * @param B
     * @param C
     * @param D
     * @return
     */
    public static double downJieZouXian(double A,double B,double C,double D){
        return sub(A,mul(div(sub(A,C),sub(A,B)),sub(A,D)));
    }
    public static void main(String[] arg){
//        System.out.println(upJieZouXian(40.57,41.32,40.80,41.67));
//        System.out.println(goldenPrice(18636.84,19406.96));
        System.out.println(downJieZouXian(46.26,45.46,46.09,44.42));
//        System.out.println(goldenPrice(40.68,42.09));
//        String a = "1";
//        String b = "2";
//        new Thread(new DeadLock(a,b),"线程a").start();
//        new Thread(new DeadLock(b,a),"线程b").start();

    }
}
class DeadLock implements Runnable{
    private String lock1;
    private String lock2;
    DeadLock(String a,String b){
        this.lock1= a;
        this.lock2= b;
    }

    @Override
    public void run() {
        synchronized (lock1){
            System.out.println(Thread.currentThread().getName()+"线程获取到锁:"+lock1);
            synchronized (lock2){
                System.out.println(Thread.currentThread().getName()+"线程获取到锁:"+lock2);

            }
        }
    }
}
