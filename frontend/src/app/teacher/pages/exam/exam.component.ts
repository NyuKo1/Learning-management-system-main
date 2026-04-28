import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { BaseComponent } from '@shared/directives/base-component';
import { EntityAttribute } from '@core/models/entity-attribute.model';
import { getSubjectWithStudyProgramDisplay } from '@core/models/subject.model';
import { SubjectService } from '@core/services/subject.service';
import { TableSelect } from '@core/models/table-select.model';
import { AuthService } from '@core/services/auth.service';
import { TableData } from '@core/models/table-data.model';
import { Exam } from '@core/models/exam.model';
import { getExamTypeDisplay } from '@core/models/exam-type.model';
import { ExamTypeService } from '@core/services/exam-type.service';
import { ExamService } from '@core/services/exam.service';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-exam',
  templateUrl: './exam.component.html',
  styleUrls: ['./exam.component.scss'],
})
export class ExamComponent extends BaseComponent<Exam> implements OnInit {
  title: string = 'Аттестации';
  name: string = 'аттестация';
  attributes: EntityAttribute[] = [
    {
      key: 'id',
      name: 'ID',
      type: 'id',
    },
    {
      key: 'name',
      name: 'Название',
      type: 'text',
      required: true,
    },
    {
      key: 'description',
      name: 'Описание',
      type: 'lob',
      required: true,
    },
    {
      key: 'minimumScore',
      name: 'Минимальный балл',
      type: 'number',
      required: true,
      validators: [Validators.min(0), Validators.max(90)],
      errorMessage: 'Минимальный балл должен быть от 0 до 90',
    },
    {
      key: 'maximumScore',
      name: 'Максимальный балл',
      type: 'number',
      required: true,
      validators: [Validators.min(1), Validators.max(90)],
      errorMessage: 'Максимальный балл должен быть от 1 до 90',
    },
    {
      key: 'examType',
      name: 'Тип аттестации',
      type: 'select',
      required: true,
      display: getExamTypeDisplay,
    },
  ];

  tableSelect: TableSelect = {
    observable: this.subjectService.getByTeacherId(
      this.authService.getTeacherId()
    ),
    display: getSubjectWithStudyProgramDisplay,
  };

  constructor(
    public override dialog: MatDialog,
    public override service: ExamService,
    public subjectService: SubjectService,
    public examTypeService: ExamTypeService,
    public authService: AuthService
  ) {
    super();
  }

  ngOnInit(): void {
    this.getPage(this.tableData);
    this.getOptions('examType', this.examTypeService);
  }

  override getPage(data?: TableData) {
    data !== undefined ? (this.tableData = data) : (data = this.tableData);
    if (!data?.select) return;

    this.service
      .getBySubjectId(data.select, data?.request)
      .subscribe((data) => {
        this.data = data;
      });
  }

  override process(value: Exam): void {
    value.subject = this.table.selectForm.value;
    super.process(value);
  }
}
