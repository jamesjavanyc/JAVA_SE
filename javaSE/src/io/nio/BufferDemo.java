package io.nio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/*
 * 一、缓冲区（Buffer）：在 Java NIO 中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
 *
 * 根据数据类型不同（boolean 除外），提供了相应类型的缓冲区：
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 *
 * 上述缓冲区的管理方式几乎一致，通过 allocate() 获取缓冲区
 *
 * 二、缓冲区存取数据的核心方法：
 * put() : 存入数据到缓冲区中
 * get() : 获取缓冲区中的数据
 *
 * flip() : 切换缓冲区模式 ，开辟写入时 limit = capacity position=0， 切换后limit移动到数据末尾的下一个位置，position=0
 * rewind() : 在读数据模式，调用rewind 可以把position归零 ，可重复读数据
 * clear() : 清空缓冲区。原数据并未被删除，只是处于被遗忘状态，即position归零，limit = capacity
 * hasRemaining remaining :判断是否还有remain的数据
 *
 * 三、缓冲区中的四个核心属性：
 * capacity : 容量，表示缓冲区中最大存储数据的容量。一旦声明不能改变。
 * limit : 界限，表示缓冲区中可以操作数据的大小。（limit 后数据不能进行读写）
 * position : 位置，表示缓冲区中正在操作数据的位置。
 *
 * mark : 标记，表示记录当前 position 的位置。可以通过 reset() 恢复到 mark 的位置
 *
 * 0 <= mark <= position <= limit <= capacity
 *
 * 四、直接缓冲区与非直接缓冲区：
 * 非直接缓冲区：通过 allocate() 方法分配缓冲区，将缓冲区建立在 JVM 的内存中
 * 直接缓冲区：通过 allocateDirect() 方法分配直接缓冲区，将缓冲区建立在物理内存中。可以提高效率
 */
public class BufferDemo {

    public void initDIRBuffer(){
        //分配直接缓冲区
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        System.out.println(buf.isDirect());
    }

    public void testMark(){
        String str = "abcde";

        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put(str.getBytes());

        buf.flip(); //切换到读数据模式：索引变成0 limit变成5（可以读取5个数据0-4）

        //读取缓冲区内的元素，从2开始读取2个
        byte[] dst = new byte[buf.limit()];
        buf.get(dst, 0, 2);
        System.out.println(new String(dst, 0, 2));
        System.out.println(buf.position());

        //mark() : 标记
        buf.mark();

        buf.get(dst, 2, 2);
        System.out.println(new String(dst, 2, 2));
        System.out.println(buf.position());

        //reset() : 恢复到 mark 的位置
        buf.reset();
        System.out.println(buf.position());

        //判断缓冲区中是否还有剩余数据
        if(buf.hasRemaining()){

            //获取缓冲区中可以操作的数量
            System.out.println(buf.remaining());
        }
    }

    public void testAPI(String[] args) throws UnsupportedEncodingException {

        String str = "baoyou";
        // 分配一个指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("buffer\tallocate\tlimit:(" + buffer.limit() + "),capacity:(" + buffer.capacity()
                + "),position:(" + buffer.position() + ")\t");
        //put() 存入数据到缓冲区中
        buffer.put(str.getBytes("UTF-8"));
        System.out.println("buffer\tput\tlimit:(" + buffer.limit() + "),capacity:(" + buffer.capacity()
                + "),position:(" + buffer.position() + ")\t");
        //切换读取数据模式
        buffer.flip();
        //get() 读取缓冲区中的数据
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println("buffer\tget\tlimit:(" + buffer.limit() + "),capacity:(" + buffer.capacity()
                + "),position:(" + buffer.position() + ")\t");
        System.out.println(new String(bytes));
        //可重复读
        buffer.rewind();
        buffer.get(bytes);
        System.out.println("buffer\trewind\tlimit:(" + buffer.limit() + "),capacity:(" + buffer.capacity()
                + "),position:(" + buffer.position() + ")\t");
        System.out.println(new String(bytes));
        //clear() : 清空缓冲区. 但是缓冲区中的数据依然存在，但是处于“被遗忘”状态
        buffer.clear();
        System.out.println("buffer\tclear\tlimit:(" + buffer.limit() + "),capacity:(" + buffer.capacity()
                + "),position:(" + buffer.position() + ")\t");
    }

}
