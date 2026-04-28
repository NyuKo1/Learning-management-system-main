import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { CourseCatalog } from '@core/models/course-catalog.model';
import { CourseLesson } from '@core/models/course-lesson.model';
import { CrmService } from '@core/services/crm.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-course-player',
  templateUrl: './course-player.component.html',
  styleUrls: ['./course-player.component.scss'],
})
export class CoursePlayerComponent implements OnInit {
  course: CourseCatalog | null = null;
  lessons: CourseLesson[] = [];
  activeLesson: CourseLesson | null = null;
  loading = true;
  error = false;

  constructor(
    private route: ActivatedRoute,
    private crmService: CrmService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    forkJoin({
      course: this.crmService.getCourseDetail(id),
      lessons: this.crmService.getLessons(id),
    }).subscribe({
      next: ({ course, lessons }) => {
        this.course = course;
        this.lessons = lessons;
        if (lessons.length > 0) {
          this.activeLesson = lessons[0];
        }
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      },
    });
  }

  selectLesson(lesson: CourseLesson): void {
    this.activeLesson = lesson;
  }

  getLevelLabel(level: string): string {
    const map: Record<string, string> = {
      BEGINNER: 'Начинающий',
      INTERMEDIATE: 'Средний',
      ADVANCED: 'Продвинутый',
    };
    return map[level] || level;
  }

  getLevelColor(level: string): string {
    const map: Record<string, string> = {
      BEGINNER: '#10b981',
      INTERMEDIATE: '#f59e0b',
      ADVANCED: '#ef4444',
    };
    return map[level] || '#6b7280';
  }

  getTags(tags: string): string[] {
    return this.crmService.getTagsArray(tags);
  }

  renderContent(content: string | undefined): SafeHtml {
    if (!content) return this.sanitizer.bypassSecurityTrustHtml('');
    let html = content
      // Headers
      .replace(/^### (.+)$/gm, '<h3>$1</h3>')
      .replace(/^## (.+)$/gm, '<h2>$1</h2>')
      .replace(/^# (.+)$/gm, '<h1>$1</h1>')
      // Bold
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      // Code blocks
      .replace(/```(\w*)\n([\s\S]*?)```/g, (_, lang, code) =>
        `<pre class="code-block"><code>${this.escapeHtml(code.trim())}</code></pre>`)
      // Inline code
      .replace(/`([^`]+)`/g, '<code class="inline-code">$1</code>')
      // Tables (simple)
      .replace(/\|(.+)\|\n\|[-| :]+\|\n((?:\|.+\|\n?)+)/g, (_, header, rows) => {
        const ths = header.split('|').filter((s: string) => s.trim()).map((s: string) => `<th>${s.trim()}</th>`).join('');
        const trs = rows.trim().split('\n').map((row: string) =>
          '<tr>' + row.split('|').filter((s: string) => s.trim()).map((s: string) => `<td>${s.trim()}</td>`).join('') + '</tr>'
        ).join('');
        return `<table class="md-table"><thead><tr>${ths}</tr></thead><tbody>${trs}</tbody></table>`;
      })
      // Unordered lists
      .replace(/^- (.+)$/gm, '<li>$1</li>')
      .replace(/(<li>.*<\/li>\n?)+/g, (m) => `<ul>${m}</ul>`)
      // Numbered lists
      .replace(/^\d+\. (.+)$/gm, '<li>$1</li>')
      // Paragraphs (double newline)
      .replace(/\n\n(?!<)/g, '</p><p>')
      // Line breaks
      .replace(/\n(?!<)/g, '<br>');
    html = `<p>${html}</p>`;
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }

  private escapeHtml(text: string): string {
    return text
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;');
  }
}
