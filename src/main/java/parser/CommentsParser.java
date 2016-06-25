package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import datastructure.Comment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by Shahbaz on 11/06/2016.
 */
public class CommentsParser {


    public static void main(String[] args) {
        String currentLine;

        try(BufferedReader bufferedReader=new BufferedReader(new FileReader("C:\\Users\\SpongeBob\\Downloads\\Sem III\\Big Data\\text-summarization-data-sample\\comments-sample.ldjson"))){
        	
        	File bodytext= new File("C:\\Users\\SpongeBob\\Downloads\\Sem III\\Big Data\\comments.jsonl");
            BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(bodytext));
            
            ObjectMapper readMapper = new ObjectMapper();
            ObjectMapper writeMapper = new ObjectMapper();
            JsonNode node;
            String content;
            String commentExtract;
            String tldr;
            String id;
            int count=0;
            int invalid_comments_count = 0;
            int invalid_tldr_count = 0;
            List<String> invalid_comments = new ArrayList<String>();
            List<String> invalid_tldr = new ArrayList<String>();
   
            Pattern c = Pattern.compile("(.+?)tl;dr", Pattern.DOTALL); //define pattern for matching tl;dr tags
            while((currentLine=bufferedReader.readLine())!=null && count <200){
                node= readMapper.readValue(currentLine,JsonNode.class);
                id = node.get("id").asText();
                content = node.get("body").asText().toLowerCase();
                tldr = content.substring(content.indexOf("tl;dr")+5, content.length());
                Matcher comment_match = c.matcher(content);
                if (content.length() < 50) {
					invalid_comments_count ++;
					invalid_comments.add(id);
					continue;
				}
                if (tldr.length() < 5) {
					invalid_tldr_count ++;
					invalid_tldr.add(id);
					continue;
				}
                
                if(comment_match.find() && comment_match.group(1).length() > 50){
                	commentExtract = comment_match.group(1);
                	Comment comment = new Comment(id,commentExtract,tldr);
                	bufferedWriter.write(writeMapper.writeValueAsString(comment));
	                bufferedWriter.newLine();
                }
               count++;
            }
            System.out.println("Invalid comments: "+ invalid_comments_count +"\n"+"Invalid tldrs: "+ invalid_tldr_count);
            System.out.println(invalid_comments.get(1));
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File not readable");
        }
    }
}
