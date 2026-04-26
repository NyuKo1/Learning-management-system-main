package ca.utoronto.lms.crm;

import ca.utoronto.lms.crm.dto.CourseDTO;
import ca.utoronto.lms.crm.repository.CourseRepository;
import ca.utoronto.lms.crm.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppDataSeeder implements ApplicationRunner {

    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (courseRepository.count() == 0) {
            seedCourses();
        }
    }

    private void seedCourses() {
        List<CourseDTO> courses = Arrays.asList(
            buildCourse(
                "Python for Beginners: Complete Course",
                "Master Python from scratch. Covers variables, data structures, functions, OOP, file handling, and real-world mini-projects. Perfect starting point for anyone new to programming.",
                "Dr. Alex Petrov", "Programming", "BEGINNER", "40h", 120,
                new BigDecimal("49.00"), new BigDecimal("89.00"),
                4.8, 15420, "Python,Beginner,OOP,Scripting", "#3b82f6", "code"),

            buildCourse(
                "Advanced Python & Machine Learning",
                "Elevate your Python skills with NumPy, Pandas, Matplotlib, Scikit-learn, and PyTorch. Build and deploy ML models to production using FastAPI and Docker.",
                "Prof. Sarah Kim", "Programming", "ADVANCED", "55h", 145,
                new BigDecimal("99.00"), new BigDecimal("179.00"),
                4.9, 8730, "Python,ML,NumPy,Pandas,PyTorch", "#8b5cf6", "psychology"),

            buildCourse(
                "Full-Stack Web Development Bootcamp",
                "Build modern web applications using Angular, Node.js, and PostgreSQL. Includes real-world portfolio projects and CI/CD pipeline setup.",
                "Sarah Mitchell", "Programming", "BEGINNER", "60h", 180,
                new BigDecimal("99.00"), new BigDecimal("199.00"),
                4.7, 8900, "Angular,Node.js,SQL,TypeScript", "#10b981", "web"),

            buildCourse(
                "Advanced Algorithms & Data Structures",
                "Master complex algorithms, graph theory, dynamic programming, and build efficient solutions for real-world engineering challenges.",
                "Dr. Elena Kovacs", "Programming", "ADVANCED", "48h", 120,
                new BigDecimal("89.00"), new BigDecimal("149.00"),
                4.9, 3420, "Algorithms,Python,C++,Graphs", "#6366f1", "memory"),

            buildCourse(
                "Calculus & Linear Algebra Mastery",
                "A comprehensive journey through differential equations, matrix operations, and vector spaces designed for engineering and CS students.",
                "Prof. Marcus Chen", "Mathematics", "INTERMEDIATE", "36h", 95,
                new BigDecimal("69.00"), new BigDecimal("119.00"),
                4.8, 5120, "Calculus,Linear Algebra,Statistics,Matrices", "#0ea5e9", "functions"),

            buildCourse(
                "Business Analytics & Data-Driven Strategy",
                "Extract insights from business data, build dashboards, and make evidence-based decisions that drive growth using Excel, SQL, and Power BI.",
                "Dr. Amara Diallo", "Business", "INTERMEDIATE", "30h", 78,
                new BigDecimal("59.00"), new BigDecimal("99.00"),
                4.6, 6700, "Analytics,Excel,PowerBI,SQL", "#f59e0b", "bar_chart"),

            buildCourse(
                "UI/UX Design Fundamentals",
                "Design beautiful, user-centric interfaces using Figma. Learn the principles of visual hierarchy, accessibility, and prototyping from scratch.",
                "Luna Park", "Design", "BEGINNER", "24h", 65,
                new BigDecimal("49.00"), new BigDecimal("89.00"),
                4.8, 11200, "Figma,UX,Prototyping,Design", "#ec4899", "palette"),

            buildCourse(
                "Quantum Physics: From Theory to Application",
                "Explore quantum mechanics, wave-particle duality, and the mathematics behind modern quantum computing concepts.",
                "Dr. Yuki Tanaka", "Science", "ADVANCED", "42h", 105,
                new BigDecimal("79.00"), null,
                4.9, 1830, "Quantum,Physics,Math,Computing", "#8b5cf6", "science")
        );

        courses.forEach(courseService::save);
    }

    private CourseDTO buildCourse(
            String title, String description, String instructor,
            String category, String level, String duration, int lessons,
            BigDecimal price, BigDecimal originalPrice,
            double rating, int studentsCount, String tags, String color, String icon) {
        CourseDTO dto = new CourseDTO();
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setInstructor(instructor);
        dto.setCategory(category);
        dto.setLevel(level);
        dto.setDuration(duration);
        dto.setLessons(lessons);
        dto.setPrice(price);
        dto.setOriginalPrice(originalPrice);
        dto.setRating(rating);
        dto.setStudentsCount(studentsCount);
        dto.setTags(tags);
        dto.setColor(color);
        dto.setIcon(icon);
        dto.setAvailable(true);
        return dto;
    }
}
