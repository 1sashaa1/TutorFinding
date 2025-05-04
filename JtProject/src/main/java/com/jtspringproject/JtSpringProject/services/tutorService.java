package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.dao.tutorDao;
import com.jtspringproject.JtSpringProject.dto.VideoDto;
import com.jtspringproject.JtSpringProject.dto.YouTubeResponse;
import com.jtspringproject.JtSpringProject.models.Tutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class tutorService {

    @Autowired
    private tutorDao tutorDao;

    @Autowired
    private YouTubeService youTubeService;

    public void addTutor(Tutors tutor) {
        try {
            this.tutorDao.saveTutor(tutor);
        } catch (DataIntegrityViolationException e) {
             throw new RuntimeException("Add tutor error");
        }
    }

    public boolean checkTutorExists(int tutorid) {
        return this.tutorDao.tutorExists(tutorid);
    }

    public Tutors getTutorId(int tutorid){
        return this.tutorDao.getTutorId(tutorid);
    }
    public List<Tutors> getTutors(){
        return this.tutorDao.getTutors();
    }

    public Tutors findByUserUsername(int id) {
        return this.tutorDao.findByUserUsername(id);
    }

    public Tutors updateTutor(Tutors tutor) {
        try {
            return this.tutorDao.updateTutor(tutor);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Update tutor error");
        }
    }
    public Tutors findById(int id) {
        return this.tutorDao.findById(id);
    }

    public Tutors getTutorWithVideos(int tutorId) {
        Tutors tutor = getTutorId(tutorId);
        if (tutor.getYoutubeChannelId() != null) {
            List<VideoDto> videos = youTubeService.getChannelVideos(
                    tutor.getYoutubeChannelId(), 5);
            tutor.setYoutubeVideos(videos);
        }
        return tutor;
    }
    public long getActiveTutorsCount() {
        return tutorDao.countActiveTutors();
    }

}
