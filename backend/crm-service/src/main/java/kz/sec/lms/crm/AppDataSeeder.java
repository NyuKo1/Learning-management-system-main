package kz.sec.lms.crm;

import kz.sec.lms.crm.client.SubjectFeignClient;
import kz.sec.lms.crm.dto.ClientDTO;
import kz.sec.lms.crm.dto.CourseLessonDTO;
import kz.sec.lms.crm.dto.CourseDTO;
import kz.sec.lms.crm.dto.LeadDTO;
import kz.sec.lms.crm.dto.SubjectSimpleDTO;
import kz.sec.lms.crm.repository.ClientRepository;
import kz.sec.lms.crm.repository.CourseLessonRepository;
import kz.sec.lms.crm.repository.CourseRepository;
import kz.sec.lms.crm.repository.LeadRepository;
import kz.sec.lms.crm.service.ClientService;
import kz.sec.lms.crm.service.CourseLessonService;
import kz.sec.lms.crm.service.CourseService;
import kz.sec.lms.crm.service.LeadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppDataSeeder implements ApplicationRunner {

    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final LeadService leadService;
    private final LeadRepository leadRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final CourseLessonService lessonService;
    private final CourseLessonRepository lessonRepository;
    private final SubjectFeignClient subjectFeignClient;

    @Override
    public void run(ApplicationArguments args) {
        if (courseRepository.count() == 0) {
            seedCoursesFromSubjectsOrStatic();
        }
        if (leadRepository.count() == 0) {
            seedLeads();
        }
        if (clientRepository.count() == 0) {
            seedClients();
        }
        if (lessonRepository.count() == 0) {
            seedLessons();
        }
    }

    // Try to seed courses from subject-service; fall back to static data if unavailable/empty
    private void seedCoursesFromSubjectsOrStatic() {
        try {
            List<SubjectSimpleDTO> subjects = subjectFeignClient.getAllSubjects();
            if (subjects != null && !subjects.isEmpty()) {
                log.info("Seeding {} CRM courses from subject-service", subjects.size());
                String[] colors = {"#3b82f6", "#8b5cf6", "#10b981", "#6366f1", "#0ea5e9", "#f59e0b", "#ec4899", "#8b5cf6"};
                String[] icons = {"book", "school", "science", "functions", "code", "bar_chart", "palette", "psychology"};
                int i = 0;
                for (SubjectSimpleDTO subject : subjects) {
                    CourseDTO dto = new CourseDTO();
                    dto.setTitle(subject.getName());
                    dto.setDescription(subject.getSyllabus() != null ? subject.getSyllabus() : subject.getName());
                    dto.setInstructor("TBD");
                    dto.setCategory("Education");
                    dto.setLevel("INTERMEDIATE");
                    dto.setDuration((subject.getEcts() != null ? subject.getEcts() * 10 : 30) + "h");
                    dto.setLessons(subject.getEcts() != null ? subject.getEcts() * 5 : 20);
                    dto.setPrice(new BigDecimal("49.00"));
                    dto.setOriginalPrice(new BigDecimal("89.00"));
                    dto.setRating(0.0);
                    dto.setStudentsCount(0);
                    dto.setTags(subject.getName());
                    dto.setColor(colors[i % colors.length]);
                    dto.setIcon(icons[i % icons.length]);
                    dto.setAvailable(true);
                    dto.setSubjectId(subject.getId());
                    courseService.save(dto);
                    i++;
                }
                return;
            }
        } catch (Exception e) {
            log.warn("Could not fetch subjects from subject-service ({}), using static course data", e.getMessage());
        }
        seedStaticCourses();
    }

    private void seedStaticCourses() {
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

    private void seedLeads() {
        List<LeadDTO> leads = Arrays.asList(
            buildLead("Айгерим Сейткали", "+7 701 234 5678", "aigerim@mail.kz", "WEBSITE", "NEW", "Интересуется Python курсом", 1L, "Python for Beginners: Complete Course"),
            buildLead("Данияр Жаксыбеков", "+7 702 345 6789", "daniyar@gmail.com", "SOCIAL", "CONTACTED", "Написал в Instagram, хочет Full-Stack", 3L, "Full-Stack Web Development Bootcamp"),
            buildLead("Мадина Нурлан", "+7 707 456 7890", "madina@yandex.ru", "COLD_CALL", "QUALIFIED", "Звонила по объявлению, бюджет есть", 1L, "Python for Beginners: Complete Course"),
            buildLead("Ерлан Сатыбалды", "+7 705 567 8901", "erlan@sec.kz", "REFERRAL", "PROPOSAL", "Пришёл от клиента Жаксыбекова, выслали КП", 2L, "Advanced Python & Machine Learning"),
            buildLead("Назгуль Абенова", "+7 701 678 9012", "nazgul@mail.kz", "WEBSITE", "WON", "Оплатила курс Python", 1L, "Python for Beginners: Complete Course"),
            buildLead("Тимур Алиев", "+7 700 789 0123", "timur@gmail.com", "SOCIAL", "LOST", "Отказался — нашёл бесплатный аналог", null, null),
            buildLead("Самал Дюсенова", "+7 702 890 1234", "samal@yandex.ru", "WEBSITE", "NEW", "Оставила заявку на UX/UI курс", 7L, "UI/UX Design Fundamentals"),
            buildLead("Бекзат Муратов", "+7 706 901 2345", "bekzat@mail.kz", "COLD_CALL", "CONTACTED", "Интересуется алгоритмами, студент КБТУ", 4L, "Advanced Algorithms & Data Structures"),
            buildLead("Аида Сакенова", "+7 701 012 3456", "aida@gmail.com", "REFERRAL", "QUALIFIED", "Знакомая Назгуль, хочет ML курс", 2L, "Advanced Python & Machine Learning"),
            buildLead("Нурлан Байжанов", "+7 707 123 4567", "nurlan@sec.kz", "WEBSITE", "PROPOSAL", "Менеджер среднего звена, нужна бизнес-аналитика", 6L, "Business Analytics & Data-Driven Strategy")
        );
        leads.forEach(leadService::save);
    }

    private void seedClients() {
        List<ClientDTO> clients = Arrays.asList(
            buildClient("Назгуль Абенова", "+7 701 678 9012", "nazgul@mail.kz", 1, new BigDecimal("49.00"), "Купила Python Beginners, прогресс 30%"),
            buildClient("Алексей Воронин", "+7 702 200 1100", "voronin@yandex.ru", 1, new BigDecimal("99.00"), "Full-Stack Bootcamp, активно учится"),
            buildClient("Жанна Сейтхали", "+7 705 300 2200", "zhanna@gmail.com", 1, new BigDecimal("99.00"), "ML курс, завершила модуль 2"),
            buildClient("Карим Омаров", "+7 700 400 3300", "karim@mail.kz", 2, new BigDecimal("138.00"), "Математика + Python, повторное обучение"),
            buildClient("Диана Парк", "+7 701 500 4400", "diana@sec.kz", 1, new BigDecimal("49.00"), "UX/UI, делает дипломный проект"),
            buildClient("Руслан Тулеев", "+7 702 600 5500", "ruslan@gmail.com", 1, new BigDecimal("89.00"), "Алгоритмы, готовится к собеседованиям"),
            buildClient("Ботагоз Ергали", "+7 707 700 6600", "botagoz@yandex.ru", 1, new BigDecimal("59.00"), "Бизнес-аналитика, внедряет PowerBI"),
            buildClient("Санжар Мухамеджанов", "+7 701 800 7700", "sanzhar@mail.kz", 1, new BigDecimal("79.00"), "Квантовая физика, научный сотрудник")
        );
        clients.forEach(clientService::save);
    }

    private void seedLessons() {
        // Python for Beginners (courseId=1)
        List<CourseLessonDTO> pythonLessons = Arrays.asList(
            buildLesson(1L, "Введение в Python и установка окружения",
                "Знакомство с языком Python, его применениями и экосистемой. Установка Python и VS Code.",
                null,
                "## Что такое Python?\n\nPython — высокоуровневый язык программирования общего назначения.\n\n### Почему Python?\n- Простой синтаксис\n- Огромная экосистема библиотек\n- Применяется в web, data science, AI\n\n```python\nprint(\"Hello, World!\")\n```",
                1, "15:20"),
            buildLesson(1L, "Переменные, типы данных и операторы",
                "Изучаем базовые типы: int, float, str, bool.",
                null,
                "## Переменные в Python\n\n```python\nname = \"Студент\"\nage = 22\ngpa = 4.7\n\nprint(f\"{name} — {age} лет, GPA: {gpa}\")\n```",
                2, "22:45"),
            buildLesson(1L, "Условия и циклы",
                "Управляющие конструкции: if/elif/else, for и while.",
                null,
                "## Условные операторы\n\n```python\nscore = 85\nif score >= 90:\n    grade = 'A'\nelif score >= 75:\n    grade = 'B'\nelse:\n    grade = 'F'\n```",
                3, "28:10"),
            buildLesson(1L, "Функции и работа с модулями",
                "Определение функций, аргументы, возвращаемые значения.",
                null,
                "## Функции\n\n```python\ndef greet(name, greeting=\"Привет\"):\n    return f\"{greeting}, {name}!\"\n\nprint(greet(\"Студент\"))\n```",
                4, "31:55"),
            buildLesson(1L, "Списки, словари и множества",
                "Коллекции данных: list, dict, set.",
                null,
                "## Список (list)\n\n```python\nstudents = [\"Айгерим\", \"Данияр\", \"Мадина\"]\nstudents.append(\"Ерлан\")\nsquares = [x**2 for x in range(10)]\n```",
                5, "35:30")
        );
        pythonLessons.forEach(lessonService::save);

        // Full-Stack Web Dev (courseId=3)
        List<CourseLessonDTO> webLessons = Arrays.asList(
            buildLesson(3L, "Введение в современный веб-стек",
                "Обзор Angular, Node.js, PostgreSQL.",
                null,
                "## Modern Full-Stack\n\n| Слой | Технология |\n|------|----------|\n| Frontend | Angular 16 |\n| Backend | Node.js + Express |\n| Database | PostgreSQL |\n\n```bash\nnpm install -g @angular/cli\n```",
                1, "18:00"),
            buildLesson(3L, "Angular: Компоненты и шаблоны",
                "Создание компонентов, data binding, директивы.",
                null,
                "## Angular компоненты\n\n```typescript\n@Component({ selector: 'app-card' })\nexport class CardComponent {\n  @Input() title: string;\n}\n```",
                2, "42:15"),
            buildLesson(3L, "Node.js: REST API с Express",
                "Создание сервера, маршруты, middleware.",
                null,
                "## Express сервер\n\n```javascript\nconst express = require('express');\nconst app = express();\n\napp.get('/api/tasks', async (req, res) => {\n  res.json({ tasks: [] });\n});\n\napp.listen(3000);\n```",
                3, "38:40"),
            buildLesson(3L, "PostgreSQL и работа с базой данных",
                "Проектирование схемы, SQL запросы.",
                null,
                "## PostgreSQL схема\n\n```sql\nCREATE TABLE tasks (\n  id SERIAL PRIMARY KEY,\n  title VARCHAR(500) NOT NULL,\n  completed BOOLEAN DEFAULT FALSE\n);\n```",
                4, "45:20")
        );
        webLessons.forEach(lessonService::save);

        // Advanced Python & ML (courseId=2)
        List<CourseLessonDTO> mlLessons = Arrays.asList(
            buildLesson(2L, "NumPy и работа с массивами",
                "Многомерные массивы, векторизация, broadcasting.",
                null,
                "## NumPy\n\n```python\nimport numpy as np\n\na = np.array([1, 2, 3, 4, 5])\nresult = a * 2 + 1  # [3, 5, 7, 9, 11]\nA = np.array([[1, 2], [3, 4]])\nB = np.array([[5, 6], [7, 8]])\nC = A @ B\n```",
                1, "40:00"),
            buildLesson(2L, "Pandas: анализ данных",
                "DataFrame, Series, загрузка данных, агрегация.",
                null,
                "## Pandas DataFrame\n\n```python\nimport pandas as pd\n\ndf = pd.read_csv('data.csv')\nprint(df.describe())\nby_cat = df.groupby('category')['revenue'].sum()\n```",
                2, "52:30"),
            buildLesson(2L, "Машинное обучение с Scikit-learn",
                "Классификация, регрессия. Оценка моделей.",
                null,
                "## ML модель\n\n```python\nfrom sklearn.ensemble import RandomForestClassifier\nfrom sklearn.metrics import accuracy_score\n\nmodel = RandomForestClassifier(n_estimators=100)\nmodel.fit(X_train, y_train)\nprint(accuracy_score(y_test, model.predict(X_test)))\n```",
                3, "58:15")
        );
        mlLessons.forEach(lessonService::save);
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

    private LeadDTO buildLead(String fullName, String phone, String email,
                               String source, String status, String notes,
                               Long interestedCourseId, String interestedCourseTitle) {
        LeadDTO dto = new LeadDTO();
        dto.setFullName(fullName);
        dto.setPhone(phone);
        dto.setEmail(email);
        dto.setSource(source);
        dto.setStatus(status);
        dto.setNotes(notes);
        dto.setInterestedCourseId(interestedCourseId);
        dto.setInterestedCourseTitle(interestedCourseTitle);
        return dto;
    }

    private ClientDTO buildClient(String fullName, String phone, String email,
                                   int totalPurchases, BigDecimal totalSpent, String notes) {
        ClientDTO dto = new ClientDTO();
        dto.setFullName(fullName);
        dto.setPhone(phone);
        dto.setEmail(email);
        dto.setTotalPurchases(totalPurchases);
        dto.setTotalSpent(totalSpent);
        dto.setHasLmsAccount(false);
        dto.setNotes(notes);
        return dto;
    }

    private CourseLessonDTO buildLesson(Long courseId, String title, String description,
                                         String videoUrl, String content, int orderIndex, String duration) {
        CourseLessonDTO dto = new CourseLessonDTO();
        dto.setCourseId(courseId);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setVideoUrl(videoUrl);
        dto.setContent(content);
        dto.setOrderIndex(orderIndex);
        dto.setDuration(duration);
        return dto;
    }
}
