import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultsPageComponent } from '@/app/components/results-page/results-page.component';

describe('ResultsComponent', () => {
  let component: ResultsPageComponent;
  let fixture: ComponentFixture<ResultsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
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
