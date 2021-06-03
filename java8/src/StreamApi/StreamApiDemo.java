package StreamApi;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamApiDemo {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
    }

    //获取
    public static void getStreamByCollection(List list){
        //通过集合获取
        //获取顺序stream 按照顺序读取
        Stream<Integer> streamList = list.stream();
        //获取parallelstream 并行同时读取数据
        Stream<Integer> parallelStream = list.parallelStream();
    }
    public static void getStreamByArrays(){
        int[] arr= {1,2,3,4,5};
        Object[] objs = new Object[5];
        for (int i = 0; i < 5; i++) {
            objs[i] = i;
        }
        //通过数组获取 类型通过数组类型决定 基本、对象
        IntStream intStream = Arrays.stream(arr);
        Stream<Object> objectStream = Arrays.stream(objs);
    }
    public static void getStreamByOf(){
        Stream<Integer> integerStream = Stream.of(1, 2, 3);
    }
    //无限流
    public static void getStreamUnlimit(){
        //迭代 ：获取从0开始的整数，限制是10个
        Stream.iterate(0,t->t+1).limit(10).forEach(System.out::println);
        //生成：
        Stream.generate(Math::random).limit(10).forEach(System.out::println);
    }

    //筛选与切片
    public static void streamTest01(List list){
        //FILTER(predicate p )  接收lambda 从流中排除元素>5
        list.stream().filter(num -> Integer.parseInt(num.toString())>5 ).forEach(System.out::println); //使用后流自动关闭
        //limit(n) 截断流 使其不超过给定的数目
        list.stream().limit(3).forEach(System.out::println);
        //skip(n) 跳过n个元素 如果元素数量不足n 返回空
        list.stream().skip(3).forEach(System.out::println);
        //distinct() 筛选 通过流所产生的元素hashcode（） 和equals（）去重
        list.add(1);
        list.stream().distinct().forEach(System.out::println);
    }


    //映射
    public static void streamMapTest(){
        List<String> list = new ArrayList<>(5);
        list.add("aa");list.add("bb");list.add("cc");list.add("dd");list.add("ee");
        //map （Function f） 接收function 做为映射函数，
        // 如果传入stream 则把这个stream作为一个整体传入新的stream
        list.stream().map(str->str.toUpperCase()).forEach(System.out::println);

        //flatMap（Function f） 把stream内部的元素逐个加入新的stream
        //使用map方法 返回的stream内部是stream 需要逐层遍历
        Stream<Stream<Character>> streamStream = list.stream().map(StreamApiDemo::fromStringToStream);
        streamStream.forEach(s->{
            s.forEach(System.out::println);
        });
        //flatmap可以直接加入一个stream
        Stream<Character> characterStream = list.stream().flatMap(StreamApiDemo::fromStringToStream);
        characterStream.forEach(System.out::println);
    }
    //输入字符串返回stream
    public static Stream<Character> fromStringToStream(String str){
        List<Character> list = new ArrayList<>();
        for (char c: str.toCharArray()) {
            list.add(c);
        }
        return list.stream();
    }

    //排序
    public static void sortStream(){
        //sorted
        ArrayList arrayListnumber = new ArrayList(5);
        arrayListnumber.add(10);
        arrayListnumber.add(50);
        arrayListnumber.add(40);
        arrayListnumber.stream().sorted().forEach(System.out::println);
        //sorted comparator
        ArrayList<Person> arrayList = new ArrayList(3);
        arrayList.add(new Person("james",30));
        arrayList.add(new Person("jason",20));
        arrayList.add(new Person("jax",60));
        arrayList.stream().sorted((p1,p2)->{
            return Integer.compare(p1.getAge(), p2.getAge());
        }).forEach(System.out::println);
    }

    //匹配与查找
    public static void findAndSearch(){
        LinkedList<Person> list= new LinkedList();
        list.add(new Person("James",30));
        list.add(new Person("Arron",20));
        list.add(new Person("Jax",60));
        list.add(new Person("Jason",40));
        list.add(new Person("Carol",30));

        //boolean anyMatch(Predicate p)
        //noneMatch allMatch
        boolean anyMatch = list.stream().anyMatch(p-> p.getAge()==40);
        System.out.println(anyMatch);

        //findFirst/ findAny 返回第一个/任意一个
        //此时 findany需要并行，否则只返回第一个  findfirst不需要并行
        Optional<Person> optionalPerson = list.parallelStream().findAny();
        System.out.println(optionalPerson);

        //count 求个数
        list.stream().filter(p->p.age>30).count();

        //max/min(Comparator)
        Stream<Integer> streamAge = list.stream().map(Person::getAge);
        Optional<Integer> maxAge = streamAge.max(Integer::compare);
        System.out.println(maxAge);
        //foreach 内部迭代
    }

    //归约
    public static void reduceStream(){
        //归约求和
        List<Integer> array =  Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Integer sum = array.stream().reduce(0, Integer::sum);
        System.out.println(sum);
    }

    //收集
    public static void streamCollect(){
        //不反回stream 返回collector api在笔记
        LinkedList<Person> list= new LinkedList();
        list.add(new Person("James",30));
        list.add(new Person("Arron",20));
        list.add(new Person("Jax",60));
        list.add(new Person("Jason",40));
        list.add(new Person("Carol",30));
        List<Person> listAgeGreater20 = list.stream().filter(p -> p.age > 20).collect(Collectors.toList());
        System.out.println(listAgeGreater20);
    }

    //Java9
    public static void newFeatureJava9(){
        LinkedList<Person> list= new LinkedList();
        list.add(new Person("James",30));
        list.add(new Person("Arron",20));
        list.add(new Person("Jax",60));
        list.add(new Person("Jason",40));
        list.add(new Person("Carol",30));
        //takewhile方法 返回stream是从第一个开始直到符合lambda的元素
        list.stream().takeWhile(p->p.age<20).forEach(System.out::println);
        //dropwhile 从第一个不满足条件的开始到结尾

        //iterator重载的方法
        //三个参数 起始值，退出判别条件，循环变量更替
        Stream.iterate(0,x->x>10,x->x+1).forEach(System.out::println);
    }

}
class Person{
    public int age;
    public String name;

    public Person(String name,int age) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }
}
