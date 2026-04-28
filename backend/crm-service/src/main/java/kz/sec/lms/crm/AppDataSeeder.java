package kz.sec.lms.crm;

import kz.sec.lms.crm.dto.ClientDTO;
import kz.sec.lms.crm.dto.CourseLessonDTO;
import kz.sec.lms.crm.dto.CourseDTO;
import kz.sec.lms.crm.dto.LeadDTO;
import kz.sec.lms.crm.repository.ClientRepository;
import kz.sec.lms.crm.repository.CourseLessonRepository;
import kz.sec.lms.crm.repository.CourseRepository;
import kz.sec.lms.crm.repository.LeadRepository;
import kz.sec.lms.crm.service.ClientService;
import kz.sec.lms.crm.service.CourseLessonService;
import kz.sec.lms.crm.service.CourseService;
import kz.sec.lms.crm.service.LeadService;
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
    private final LeadService leadService;
    private final LeadRepository leadRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final CourseLessonService lessonService;
    private final CourseLessonRepository lessonRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (courseRepository.count() == 0) {
            seedCourses();
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
                "## Что такое Python?\n\nPython — высокоуровневый язык программирования общего назначения, созданный Гвидо ван Россумом в 1991 году.\n\n### Почему Python?\n- Простой синтаксис, похожий на псевдокод\n- Огромная экосистема библиотек\n- Применяется в web, data science, AI, автоматизации\n\n### Установка\n1. Скачайте Python с [python.org](https://python.org)\n2. Установите VS Code\n3. Установите расширение Python для VS Code\n\n```python\nprint(\"Hello, World!\")\n```\n\nПоздравляем, вы написали первую программу!",
                1, "15:20"),
            buildLesson(1L, "Переменные, типы данных и операторы",
                "Изучаем базовые типы: int, float, str, bool. Арифметические и логические операторы.",
                null,
                "## Переменные в Python\n\n```python\nname = \"Айгерим\"\nage = 22\ngpa = 4.7\nis_student = True\n\nprint(f\"{name} — {age} лет, GPA: {gpa}\")\n```\n\n### Типы данных\n| Тип | Пример | Описание |\n|-----|--------|----------|\n| int | `42` | Целое число |\n| float | `3.14` | Дробное число |\n| str | `\"hello\"` | Строка |\n| bool | `True` | Логическое значение |\n\n### Операторы\n```python\n# Арифметика\nresult = 10 + 3   # 13\nresult = 10 ** 2  # 100 (степень)\nresult = 10 % 3   # 1 (остаток)\n```",
                2, "22:45"),
            buildLesson(1L, "Условия и циклы",
                "Управляющие конструкции: if/elif/else, for и while циклы.",
                null,
                "## Условные операторы\n\n```python\nscore = 85\n\nif score >= 90:\n    grade = 'A'\nelif score >= 75:\n    grade = 'B'\nelif score >= 60:\n    grade = 'C'\nelse:\n    grade = 'F'\n\nprint(f\"Оценка: {grade}\")\n```\n\n## Циклы\n\n```python\n# for по списку\nfruits = [\"яблоко\", \"банан\", \"апельсин\"]\nfor fruit in fruits:\n    print(fruit)\n\n# while\ncount = 0\nwhile count < 5:\n    print(count)\n    count += 1\n\n# range\nfor i in range(1, 11):\n    print(i * i)\n```",
                3, "28:10"),
            buildLesson(1L, "Функции и работа с модулями",
                "Определение функций, аргументы, возвращаемые значения, стандартная библиотека.",
                null,
                "## Функции\n\n```python\ndef greet(name, greeting=\"Привет\"):\n    return f\"{greeting}, {name}!\"\n\nprint(greet(\"Данияр\"))\nprint(greet(\"Мадина\", \"Здравствуй\"))\n\n# Lambda функции\nsquare = lambda x: x ** 2\nprint(square(7))  # 49\n```\n\n## Стандартная библиотека\n\n```python\nimport math\nimport random\n\nprint(math.sqrt(144))     # 12.0\nprint(math.pi)            # 3.14159...\nprint(random.randint(1, 100))  # случайное число\n```",
                4, "31:55"),
            buildLesson(1L, "Списки, словари и множества",
                "Коллекции данных: list, dict, set. Методы, comprehension, итерация.",
                null,
                "## Список (list)\n\n```python\nstudents = [\"Айгерим\", \"Данияр\", \"Мадина\"]\nstudents.append(\"Ерлан\")\nstudents.sort()\n\n# List comprehension\nsquares = [x**2 for x in range(10)]\neven = [x for x in range(20) if x % 2 == 0]\n```\n\n## Словарь (dict)\n\n```python\nstudent = {\n    \"name\": \"Назгуль\",\n    \"age\": 21,\n    \"courses\": [\"Python\", \"ML\"]\n}\n\nprint(student[\"name\"])\nstudent[\"gpa\"] = 4.5\n\nfor key, value in student.items():\n    print(f\"{key}: {value}\")\n```",
                5, "35:30")
        );
        pythonLessons.forEach(lessonService::save);

        // Full-Stack Web Dev (courseId=3)
        List<CourseLessonDTO> webLessons = Arrays.asList(
            buildLesson(3L, "Введение в современный веб-стек",
                "Обзор Angular, Node.js, PostgreSQL. Архитектура полностековых приложений.",
                null,
                "## Современный Full-Stack\n\n### Что мы будем строить?\nВ этом курсе вы создадите три полноценных веб-приложения:\n1. **TaskBoard** — менеджер задач с авторизацией\n2. **ShopAPI** — REST API интернет-магазина\n3. **ChatApp** — real-time чат на WebSockets\n\n### Технологический стек\n| Слой | Технология | Роль |\n|------|-----------|------|\n| Frontend | Angular 16 | UI компоненты, маршрутизация |\n| Backend | Node.js + Express | REST API, бизнес-логика |\n| Database | PostgreSQL | Хранение данных |\n| Auth | JWT | Авторизация |\n\n### Установка окружения\n```bash\nnpm install -g @angular/cli\nnpm install -g nodemon\n```",
                1, "18:00"),
            buildLesson(3L, "Angular: Компоненты и шаблоны",
                "Создание компонентов, работа с шаблонами, data binding, директивы.",
                null,
                "## Angular компоненты\n\n```typescript\n@Component({\n  selector: 'app-task-card',\n  template: `\n    <div class=\"card\" [class.done]=\"task.completed\">\n      <h3>{{ task.title }}</h3>\n      <p>{{ task.description }}</p>\n      <button (click)=\"toggleComplete()\">Готово</button>\n    </div>\n  `\n})\nexport class TaskCardComponent {\n  @Input() task: Task;\n  @Output() taskUpdated = new EventEmitter<Task>();\n\n  toggleComplete() {\n    this.task.completed = !this.task.completed;\n    this.taskUpdated.emit(this.task);\n  }\n}\n```\n\n## Data Binding\n- `{{ value }}` — интерполяция\n- `[property]=\"value\"` — property binding\n- `(event)=\"handler()\"` — event binding\n- `[(ngModel)]=\"value\"` — two-way binding",
                2, "42:15"),
            buildLesson(3L, "Node.js: REST API с Express",
                "Создание сервера, маршруты, middleware, валидация данных.",
                null,
                "## Express сервер\n\n```javascript\nconst express = require('express');\nconst app = express();\n\napp.use(express.json());\n\n// GET все задачи\napp.get('/api/tasks', async (req, res) => {\n  const tasks = await Task.findAll();\n  res.json(tasks);\n});\n\n// POST новая задача\napp.post('/api/tasks', async (req, res) => {\n  const { title, description } = req.body;\n  if (!title) return res.status(400).json({ error: 'Title required' });\n  \n  const task = await Task.create({ title, description });\n  res.status(201).json(task);\n});\n\napp.listen(3000, () => console.log('API running on :3000'));\n```",
                3, "38:40"),
            buildLesson(3L, "PostgreSQL и работа с базой данных",
                "Проектирование схемы, SQL запросы, ORM Sequelize.",
                null,
                "## PostgreSQL схема\n\n```sql\nCREATE TABLE users (\n  id SERIAL PRIMARY KEY,\n  email VARCHAR(255) UNIQUE NOT NULL,\n  password_hash VARCHAR(255) NOT NULL,\n  created_at TIMESTAMP DEFAULT NOW()\n);\n\nCREATE TABLE tasks (\n  id SERIAL PRIMARY KEY,\n  title VARCHAR(500) NOT NULL,\n  description TEXT,\n  completed BOOLEAN DEFAULT FALSE,\n  user_id INTEGER REFERENCES users(id),\n  created_at TIMESTAMP DEFAULT NOW()\n);\n\n-- Выборка с JOIN\nSELECT t.*, u.email\nFROM tasks t\nJOIN users u ON t.user_id = u.id\nWHERE t.completed = FALSE\nORDER BY t.created_at DESC;\n```",
                4, "45:20")
        );
        webLessons.forEach(lessonService::save);

        // Advanced Python & ML (courseId=2)
        List<CourseLessonDTO> mlLessons = Arrays.asList(
            buildLesson(2L, "NumPy и работа с массивами",
                "Многомерные массивы, векторизация, broadcasting, линейная алгебра.",
                null,
                "## NumPy — фундамент Data Science\n\n```python\nimport numpy as np\n\n# Создание массивов\na = np.array([1, 2, 3, 4, 5])\nmatrix = np.zeros((3, 4))\nrandom = np.random.randn(100, 100)\n\n# Векторизация (без циклов!)\nresult = a * 2 + 1  # [3, 5, 7, 9, 11]\n\n# Матричные операции\nA = np.array([[1, 2], [3, 4]])\nB = np.array([[5, 6], [7, 8]])\nC = A @ B  # Матричное произведение\n\n# Статистика\nprint(np.mean(random))  # ~0\nprint(np.std(random))   # ~1\n```",
                1, "40:00"),
            buildLesson(2L, "Pandas: анализ данных",
                "DataFrame, Series, загрузка данных, агрегация, визуализация.",
                null,
                "## Pandas DataFrame\n\n```python\nimport pandas as pd\n\n# Загрузка данных\ndf = pd.read_csv('sales.csv')\n\n# Первичный анализ\nprint(df.shape)       # (строки, столбцы)\nprint(df.describe())  # статистика\nprint(df.isnull().sum())  # пропущенные значения\n\n# Фильтрация\nhigh_sales = df[df['revenue'] > 10000]\ndf_clean = df.dropna(subset=['email'])\n\n# Группировка\nby_category = df.groupby('category')['revenue'].agg(['sum', 'mean', 'count'])\n\n# Сохранение\ndf.to_csv('result.csv', index=False)\n```",
                2, "52:30"),
            buildLesson(2L, "Машинное обучение с Scikit-learn",
                "Классификация, регрессия, кластеризация. Оценка моделей, кросс-валидация.",
                null,
                "## Первая ML модель\n\n```python\nfrom sklearn.model_selection import train_test_split\nfrom sklearn.ensemble import RandomForestClassifier\nfrom sklearn.metrics import accuracy_score, classification_report\nimport pandas as pd\n\n# Данные\ndf = pd.read_csv('churn.csv')\nX = df.drop('churned', axis=1)\ny = df['churned']\n\n# Разбивка на train/test\nX_train, X_test, y_train, y_test = train_test_split(\n    X, y, test_size=0.2, random_state=42\n)\n\n# Обучение\nmodel = RandomForestClassifier(n_estimators=100)\nmodel.fit(X_train, y_train)\n\n# Оценка\npredictions = model.predict(X_test)\nprint(f\"Accuracy: {accuracy_score(y_test, predictions):.2%}\")\nprint(classification_report(y_test, predictions))\n```",
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
