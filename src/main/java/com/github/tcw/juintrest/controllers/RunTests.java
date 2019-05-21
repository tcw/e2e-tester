package com.github.tcw.juintrest.controllers;

import com.github.tcw.juintrest.listeners.ReportListener;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class RunTests {

    ReportListener listener = new ReportListener();


    @RequestMapping("/run/{packagePath}")
    public ResponseEntity<String> run(@PathVariable("packagePath") String packagePath) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage(packagePath))
                .build();
        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        return ResponseEntity.ok(listener.getReport());
    }

    @RequestMapping("/suites")
    public ResponseEntity<List<String>> listPackages() {
        try {
            ImmutableSet<ClassPath.ClassInfo> classInfos = ClassPath.from(ClassLoader.getSystemClassLoader())
                    .getTopLevelClassesRecursive("com.github.tcw.juintrest.remotetest");
            List<String> paths = classInfos.asList().stream().map(a -> a.getPackageName()).collect(Collectors.toList());
            List<String> links = paths.stream().map(path -> linkTo(methodOn(RunTests.class)
                    .run(path)).withSelfRel().getHref()).collect(Collectors.toList());
            return ResponseEntity.ok(links);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(new ArrayList<>());
    }


}
