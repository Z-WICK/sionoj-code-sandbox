package com.sion.sionojcodesandbox;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@Slf4j
@SpringBootTest
public class DockerDemo {


    public static void main(String[] args) {

        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        SearchImagesCmd searchImagesCmd = dockerClient.searchImagesCmd("java8").withLimit(10);
        List<SearchItem> searchItemList = searchImagesCmd.exec();
        for (SearchItem searchItem : searchItemList) {
            String description = searchItem.getDescription();
            String name = searchItem.getName();
            System.out.println("容器名: "+name+"\n 描述信息"+description);
        }




    }

}
