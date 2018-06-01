package chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * Created by 58 on 2018/5/29.
 */
public class ByteBufDemo {

    public static void main(String[] args) throws IllegalAccessException {
        testByteBuf();
    }

    private static void alloctDirectMem(){
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);

    }

    private static void testIntern() throws IllegalAccessException {
        int _1M = 1024 * 1024;
        Field unsafeFileld = Unsafe.class.getFields()[0];
        unsafeFileld.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeFileld.get(null);
        while(true){
            unsafe.allocateMemory(_1M);
        }
    }

    private static void testBuma(){
        long num = System.currentTimeMillis();
        System.out.println(num);
        for(int i = 1; i <= 64 ; i ++){
            System.out.print(num & 1);
            System.out.print(" ");
            num >>>= 1;
        }
    }

    private static void testByteBuf(){
        ByteBuf buf = Unpooled.copiedBuffer("buf demo", CharsetUtil.UTF_8);
        System.out.println(buf.readerIndex());
        while(buf.isReadable()){
            System.out.print((char)buf.readByte() + " ");
            System.out.println(buf.readerIndex());
        }

        System.out.println();
        String s = buf.toString();
        byte[] array  = new byte[8];
        buf.getBytes(0, array);
        array = buf.array();
        String s1 = new String(array);
        System.out.println(s);
        System.out.println(s1);
    }
}
