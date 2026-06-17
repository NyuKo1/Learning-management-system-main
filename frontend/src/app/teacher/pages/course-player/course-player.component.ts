import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeHtml, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { CourseCatalog } from '@core/models/course-catalog.model';
import { CourseLesson } from '@core/models/course-lesson.model';
import { CrmService } from '@core/services/crm.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-teacher-course-player',
  templateUrl: './course-player.component.html',
  styleUrls: ['./course-player.component.scss'],
})
export class TeacherCoursePlayerComponent implements OnInit {
  course: CourseCatalog | null = null;
  lessons: CourseLesson[] = [];
  activeLesson: CourseLesson | null = null;
  activeVideoFile: string | null = null;
  activeVideoEmbed: SafeResourceUrl | null = null;
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
        if (lessons.length > 0) { this.activeLesson = lessons[0]; this.updateVideo(); }
        this.loading = false;
      },
      error: () => { this.error = true; this.loading = false; },
    });
  }

  selectLesson(lesson: CourseLesson): void { this.activeLesson = lesson; this.updateVideo(); }

  private updateVideo(): void {
    this.activeVideoFile = null;
    this.activeVideoEmbed = null;
    const url = this.activeLesson?.videoUrl?.trim();
    if (!url) return;

    if (/\.(mp4|webm|ogg|ogv|mov|m4v)(\?.*)?$/i.test(url)) {
      this.activeVideoFile = url;
    } else {
      this.activeVideoEmbed = this.sanitizer.bypassSecurityTrustResourceUrl(
        this.toEmbedUrl(url)
      );
    }
  }

  private toEmbedUrl(url: string): string {
    const yt = url.match(
      /(?:youtube(?:-nocookie)?\.com\/(?:watch\?(?:.*&)?v=|embed\/|shorts\/|live\/)|youtu\.be\/)([\w-]{11})/
    );
    if (yt) return `https://www.youtube.com/embed/${yt[1]}`;

    const vimeo = url.match(/vimeo\.com\/(?:video\/)?(\d+)/);
    if (vimeo) return `https://player.vimeo.com/video/${vimeo[1]}`;

    return url;
  }

  getLevelLabel(level: string): string {
    const map: Record<string, string> = { BEGINNER: 'Начинающий', INTERMEDIATE: 'Средний', ADVANCED: 'Продвинутый' };
    return map[level] || level;
  }

  getLevelColor(level: string): string {
    const map: Record<string, string> = { BEGINNER: '#10b981', INTERMEDIATE: '#f59e0b', ADVANCED: '#ef4444' };
    return map[level] || '#6b7280';
  }

  getTags(tags: string): string[] {
    return this.crmService.getTagsArray(tags);
  }

  renderContent(content: string | undefined): SafeHtml {
    if (!content) return this.sanitizer.bypassSecurityTrustHtml('');
    let html = content
      .replace(/^### (.+)$/gm, '<h3>$1</h3>')
      .replace(/^## (.+)$/gm, '<h2>$1</h2>')
      .replace(/^# (.+)$/gm, '<h1>$1</h1>')
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      .replace(/```(\w*)\n([\s\S]*?)```/g, (_, lang, code) =>
        `<pre class="code-block"><code>${this.escapeHtml(code.trim())}</code></pre>`)
      .replace(/`([^`]+)`/g, '<code class="inline-code">$1</code>')
      .replace(/^- (.+)$/gm, '<li>$1</li>')
      .replace(/(<li>.*<\/li>\n?)+/g, (m) => `<ul>${m}</ul>`)
      .replace(/^\d+\. (.+)$/gm, '<li>$1</li>')
      .replace(/\n\n(?!<)/g, '</p><p>')
      .replace(/\n(?!<)/g, '<br>');
    return this.sanitizer.bypassSecurityTrustHtml(`<p>${html}</p>`);
  }

  private escapeHtml(text: string): string {
    return text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  }
}
