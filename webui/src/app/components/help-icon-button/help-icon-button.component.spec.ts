import { AppModule } from '@/app/app.module';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HelpIconButtonComponent } from './help-icon-button.component';

describe('HelpIconButtonComponent', () => {
  let component: HelpIconButtonComponent;
  let fixture: ComponentFixture<HelpIconButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppModule],
      declarations: [HelpIconButtonComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(HelpIconButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
