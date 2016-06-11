package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

/**
 * Created by Shahbaz on 11/06/2016.
 */
public class SubmissionsParser {
    public static void main(String[] args) {
        String currentLine;
        String pattern="tl;dr";
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader("D:\\Semester\\summarization\\submissions-sample.ldjson"))){

            File selftext= new File("D:\\Semester\\summarization\\selftext.ldjson");
            BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(selftext));
            bufferedWriter.write("");
            ObjectMapper readMapper = new ObjectMapper();
            JsonNode node;
            String content;
            int count=0;
            while((currentLine=bufferedReader.readLine())!=null && count<200){
                node= readMapper.readValue(currentLine,JsonNode.class);
                content=node.get("selftext").asText();
                if(content.toLowerCase().contains(pattern)){
                    bufferedWriter.write("{ " +content+ " }");
                    bufferedWriter.newLine();

                }
                count++;
            }

            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File not readable");
        }
    }
}
