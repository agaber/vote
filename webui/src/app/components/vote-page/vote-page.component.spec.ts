import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VotePageComponent } from '@/app/components/vote-page/vote-page.component';
import { AppModule } from '@/app/app.module';

describe('VoteComponentComponent', () => {
  let component: VotePageComponent;
  let fixture: ComponentFixture<VotePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppModule],
      declarations: [VotePageComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(VotePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
