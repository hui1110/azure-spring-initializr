package com.azure.spring.initializr.generator.gitworkflow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GithubWorkflow {

    private String name;

    private Trigger to;

    private Map<String, Job> jobs = new HashMap<>();

    public void write(PrintWriter writer) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.writeValue(writer, this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Trigger getTo() {
        return to;
    }

    public void setTo(Trigger to) {
        this.to = to;
    }

    public Map<String, Job> getJobs() {
        return jobs;
    }

    public void setJobs(Map<String, Job> jobs) {
        this.jobs = jobs;
    }

    public static class Trigger {

        private Push push;
        private PullRequest pullRequest;

        public static class Push {
            private List<String> branches;

            public Push() {
                this.branches = new ArrayList<>();
            }

            public void add(String... branches) {
                this.branches.addAll(Arrays.asList(branches));
            }

            public List<String> getBranches() {
                return branches;
            }

            public void setBranches(List<String> branches) {
                this.branches = branches;
            }
        }

        public static class PullRequest {
            private List<String> branches;

            public PullRequest() {
                this.branches = new ArrayList<>();
            }

            public void add(String... branches) {
                this.branches.addAll(Arrays.asList(branches));
            }

            public List<String> getBranches() {
                return branches;
            }

            public void setBranches(List<String> branches) {
                this.branches = branches;
            }
        }

        public Push getPush() {
            return push;
        }

        public void setPush(Push push) {
            this.push = push;
        }

        public PullRequest getPullRequest() {
            return pullRequest;
        }

        public void setPullRequest(PullRequest pullRequest) {
            this.pullRequest = pullRequest;
        }
    }

    public static class Job {
        private String runsOn;
        private List<Step> steps;

        public String getRunsOn() {
            return runsOn;
        }

        public void setRunsOn(String runsOn) {
            this.runsOn = runsOn;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public void setSteps(List<Step> steps) {
            this.steps = steps;
        }

        public static class Step {
            private String uses;
            private String name;
            private String run;
            private Map<String, String> with;

            public String getUses() {
                return uses;
            }

            public void setUses(String uses) {
                this.uses = uses;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRun() {
                return run;
            }

            public void setRun(String run) {
                this.run = run;
            }

            public Map<String, String> buildWith() {
                if(this.with == null) {
                    this.with = new HashMap<>();
                }
                return this.with;
            }

            public Map<String, String> getWith() {
                return with;
            }

            public void setWith(Map<String, String> with) {
                this.with = with;
            }
        }
    }
}
