import { AppModule } from '@/app/app.module';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateElectionComponent } from '@/app/components/create-election/create-election.component';

describe('CreateElectionComponent', () => {
  let component: CreateElectionComponent;
  let fixture: ComponentFixture<CreateElectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppModule],
      declarations: [CreateElectionComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(CreateElectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
