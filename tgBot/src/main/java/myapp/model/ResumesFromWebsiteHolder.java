package myapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResumesFromWebsiteHolder {
    private static List<Resume> resumes = new ArrayList<>();

    public static List<Resume> getResumes() {
        return resumes;
    }

    public static void setResumes(List<Resume> resumes) {
        ResumesFromWebsiteHolder.resumes = resumes;
    }
}
