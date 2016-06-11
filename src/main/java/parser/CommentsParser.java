package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahbaz on 11/06/2016.
 */
public class CommentsParser {


    public static void main(String[] args) {
        String currentLine;
        String pattern="tl;dr";

        try(BufferedReader bufferedReader=new BufferedReader(new FileReader("D:\\Semester\\summarization\\comments-sample.ldjson"))){

            File bodytext= new File("D:\\Semester\\summarization\\comments.ldjson");
            BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(bodytext));
            bufferedWriter.write("");
            ObjectMapper readMapper = new ObjectMapper();
            JsonNode node;
            String content;
            int count=0;
            while((currentLine=bufferedReader.readLine())!=null && count<200){
                node= readMapper.readValue(currentLine,JsonNode.class);
                content=node.get("body").asText();
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
