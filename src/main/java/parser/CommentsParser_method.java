package parser;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class CommentsParser_method {
    static ObjectMapper readMapper = new ObjectMapper();
    static ObjectMapper writeMapper = new ObjectMapper();
    static JsonNode node;

    static File bodytext= new File("C:\\Users\\SpongeBob\\Downloads\\Sem III\\Big Data\\comments_oneLine.jsonl");
    
    
    int totalLines=0;
    static int valid_comments_count=0;
    static int valid_tdldr_count =0;
    static int lines_with_tldr =0;
    static String id;
	static String content;
	static String tldr;
	static String commentExtract;
	String body;
	static String comment_json;
    
    static Pattern c = Pattern.compile("(.+?)tl;dr", Pattern.DOTALL); //define pattern for matching tl;dr tags
    
    public static void main(String[] args) throws IOException {
    	BufferedReader bufferedReader=new BufferedReader(new FileReader("C:\\Users\\SpongeBob\\Downloads\\Sem III\\Big Data\\text-summarization-data-sample\\comments-sample.ldjson"));
    	String comment_final="", current_line;
    	int count = 0, no_comments=0;
    	while((current_line=bufferedReader.readLine())!=null && count <10){
    		comment_final = commentsParser(current_line);
    		count++;
    		System.out.println(comment_final);
    	}
    	bufferedReader.close();

    }
        
    public static String commentsParser(String line) throws IOException{
    	//BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(bodytext));
    	node = readMapper.readValue(line, JsonNode.class);
        id = node.get("id").asText();
        content = node.get("body").asText().toLowerCase();
        if(content.contains("tl;dr")) {
            lines_with_tldr++;

            tldr = content.substring(content.indexOf("tl;dr") + 5, content.length());
            if (tldr.length() > 5) {
                valid_tdldr_count++;
                Matcher comment_match = c.matcher(content);
                if (comment_match.find() && comment_match.group(1).length() > 50) {
                    valid_comments_count++;
                    commentExtract = comment_match.group(1);
                    Comment comment = new Comment(id, commentExtract, tldr);
                    comment_json = writeMapper.writeValueAsString(comment);
                    //bufferedWriter.write(writeMapper.writeValueAsString(comment));
	                //bufferedWriter.newLine();
	                
                }
            }
        }return comment_json;
    }
}
