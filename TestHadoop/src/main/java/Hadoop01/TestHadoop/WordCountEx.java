package Hadoop01.TestHadoop;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCountEx{ 

	static class MyMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context)
                throws IOException, InterruptedException {

            // 分割字符串
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {

                String tmp = itr.nextToken();

                String regex = "<artifactId>.*?</artifactId>";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(tmp);

                String ss = null;

                while (matcher.find()){
                    ss = matcher.group();
                    //word.set(ss.substring(ss.indexOf(">")+1,ss.indexOf("/")-1));
                    word.set(ss);
                    context.write(word, one);
                }

            }
        }

    }
	
	static class MyReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();
        private Text keyEx = new Text();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values,
                Reducer<Text, IntWritable, Text, IntWritable>.Context context)
                        throws IOException, InterruptedException {

            int sum = 0;
            for (IntWritable val : values) {
                // 将map的结果放大，乘以2
                sum += val.get();
            }
            result.set(sum);
            // 自定义输出key
            keyEx.set(key.toString());
            context.write(keyEx, result);
        }

    }
	
	public static void main(String[] args) throws Exception {

        //配置信息
        Configuration conf = new Configuration();
        String jobName = WordCountEx.class.getSimpleName();
        
        //job名称
        Job job = Job.getInstance(conf,jobName);
        job.setJarByClass(WordCountEx.class);
 
        
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        job.setReducerClass(MyReduce.class);       
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        

        
        //input output解析
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
       
        //输入、输出path
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        job.waitForCompletion(true);
        

    }
}
