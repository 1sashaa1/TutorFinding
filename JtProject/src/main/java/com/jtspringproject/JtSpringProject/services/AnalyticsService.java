package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.dao.lessonDao;
import com.jtspringproject.JtSpringProject.dao.subjectDao;
import com.jtspringproject.JtSpringProject.dao.userDao;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final userDao userDao;
    private final lessonDao lessonDao;
    private final subjectDao subjectDao;

    public AnalyticsService(userDao userDao, lessonDao lessonDao, com.jtspringproject.JtSpringProject.dao.subjectDao subjectDao) {
        this.userDao = userDao;
        this.lessonDao = lessonDao;
        this.subjectDao = subjectDao;
    }

    public double getUserGrowthPercent() {
        long currentMonthUsers = userDao.countUsersInMonth(YearMonth.now());
        long previousMonthUsers = userDao.countUsersInMonth(YearMonth.now().minusMonths(1));

        if (previousMonthUsers == 0) {
            return currentMonthUsers > 0 ? 100.0 : 0.0;
        }

        return ((double)(currentMonthUsers - previousMonthUsers) / previousMonthUsers) * 100.0;
    }

    public Map<String, Long> getPopularSubjects(int limit) {
        return subjectDao.getPopularSubjects(limit);
    }
}
