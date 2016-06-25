package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import datastructure.Comment;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shahbaz on 11/06/2016.
 */

public class SubmissionsParser {


    public static void main(String[] args) {
        String currentLine;

        try(BufferedReader bufferedReader=new BufferedReader(new FileReader("C:\\Users\\SpongeBob\\Downloads\\Sem III\\Big Data\\text-summarization-data-sample\\submissions-sample.ldjson"))){
        	
        	File bodytext= new File("C:\\Users\\SpongeBob\\Downloads\\Sem III\\Big Data\\submissions.jsonl");
            //BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(bodytext), "UTF8");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bodytext), "UTF8"));
            ObjectMapper readMapper = new ObjectMapper();
            ObjectMapper writeMapper = new ObjectMapper();
            JsonNode node;
            String content;
            String submissionExtract;
            String tldr;
            String id;
            int count=0;
   
            Pattern c = Pattern.compile("(.+?)tl;dr", Pattern.DOTALL); //define pattern for matching tl;dr tags
            while((currentLine=bufferedReader.readLine())!=null && count < 200){
                node = readMapper.readValue(currentLine,JsonNode.class);
                id = node.get("id").asText();
                content = node.get("selftext").asText().toLowerCase();
                tldr = content.substring(content.indexOf("tl;dr")+5, content.length());
                Matcher comment_match = c.matcher(content);
                if(comment_match.find() && comment_match.group(1).length() > 10){
                	submissionExtract = comment_match.group(1);
                	Comment comment = new Comment(id,submissionExtract,tldr);
                	bufferedWriter.write(writeMapper.writeValueAsString(comment));
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



/*public class SubmissionsParser {
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
*/