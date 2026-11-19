import java.io.*;
public class BufferCounter {
 static long countUnbuffered(File f)throws IOException{
  long lines=0;try(InputStream in=new FileInputStream(f)){int b;while((b=in.read())!=-1)if(b==10)lines++;}return lines;
 }
 static long countBuffered(File f)throws IOException{
  long lines=0;byte[] buffer=new byte[64*1024];
  try(InputStream in=new BufferedInputStream(new FileInputStream(f),buffer.length)){
   int n;while((n=in.read(buffer))!=-1)for(int i=0;i<n;i++)if(buffer[i]==10)lines++;
  }return lines;
 }
 public static void main(String[] a)throws Exception{
  File f=new File(a[0]);long t=System.nanoTime();long x=countUnbuffered(f);long u=System.nanoTime()-t;
  t=System.nanoTime();long y=countBuffered(f);long b=System.nanoTime()-t;
  System.out.printf("lines=%d/%d unbuffered=%d ns buffered=%d ns%n",x,y,u,b);
 }
}