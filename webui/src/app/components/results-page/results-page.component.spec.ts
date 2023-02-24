import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsPageComponent } from '@/app/components/results-page/results-page.component';
import { AppModule } from '@/app/app.module';

describe('ResultsComponent', () => {
  let component: ResultsPageComponent;
  let fixture: ComponentFixture<ResultsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppModule],
      declarations: [ResultsPageComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ResultsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
