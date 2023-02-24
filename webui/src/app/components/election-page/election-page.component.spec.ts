import { AppModule } from '@/app/app.module';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ElectionPageComponent } from '@/app/components/election-page/election-page.component';

describe('CreateElectionComponent', () => {
  let component: ElectionPageComponent;
  let fixture: ComponentFixture<ElectionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppModule],
      declarations: [ElectionPageComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ElectionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
