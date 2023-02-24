import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VotePageComponent } from '@/app/components/vote-page/vote-page.component';

describe('VoteComponentComponent', () => {
  let component: VotePageComponent;
  let fixture: ComponentFixture<VotePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
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
