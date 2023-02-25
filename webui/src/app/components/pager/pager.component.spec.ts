import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';

import { AppMaterialModule } from '@/app/app-material.module';
import { PagerComponent } from '@/app/components/pager/pager.component';

describe('PagerComponent', () => {
  let component: PagerComponent;
  let de: DebugElement;
  let fixture: ComponentFixture<PagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppMaterialModule],
      declarations: [PagerComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(PagerComponent);
    component = fixture.componentInstance;
    de = fixture.debugElement;
    fixture.detectChanges();
    fixture.autoDetectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('previous button', () => {
    it('should be disabled if on the first page', () => {
      component.pageNumber = 1;
      component.numberOfPages = 10;
      fixture.detectChanges();
      const previousButton = de.query(By.css('#previousButton'));
      expect(previousButton.nativeElement.getAttribute('disabled'))
        .toEqual('true');
    });

    it('should be disabled if there is only one page', () => {
      component.pageNumber = 1;
      component.numberOfPages = 1;
      fixture.detectChanges();
      const previousButton = de.query(By.css('#previousButton'));
      expect(previousButton.nativeElement.getAttribute('disabled'))
        .toEqual('true');
    });

    it('should be enabled on the second page', () => {
      component.pageNumber = 2;
      component.numberOfPages = 10;
      fixture.detectChanges();
      const previousButton = de.query(By.css('#previousButton'));
      expect(previousButton.nativeElement.getAttribute('disabled')).toBeNull();
    });

    it('should emit an event when clicked', () => {
      component.pageNumber = 2;
      component.numberOfPages = 10;
      fixture.detectChanges();

      spyOn(component.previousButtonClick, 'emit');
      spyOn(component.nextButtonClick, 'emit');

      const previousButton = de.query(By.css('#previousButton'));
      previousButton.nativeElement.dispatchEvent(new Event('click'));
      fixture.detectChanges();

      expect(component.nextButtonClick.emit).not.toHaveBeenCalled();
      expect(component.previousButtonClick.emit).toHaveBeenCalled();
    });
  });

  describe('next button', () => {
    it('should be disabled if on the last page', () => {
      component.pageNumber = 10;
      component.numberOfPages = 10;
      fixture.detectChanges();
      const nextButton = de.query(By.css('#nextButton'));
      expect(nextButton.nativeElement.getAttribute('disabled')).toEqual('true');
    });

    it('should be disabled if there is only one page', () => {
      component.pageNumber = 1;
      component.numberOfPages = 1;
      fixture.detectChanges();
      const nextButton = de.query(By.css('#nextButton'));
      expect(nextButton.nativeElement.getAttribute('disabled')).toEqual('true');
    });

    it('should be enabled when not on the last page', () => {
      component.pageNumber = 2;
      component.numberOfPages = 10;
      fixture.detectChanges();
      const nextButton = de.query(By.css('#nextButton'));
      expect(nextButton.nativeElement.getAttribute('disabled')).toBeNull();
    });

    it('should emit an event when clicked', () => {
      component.pageNumber = 2;
      component.numberOfPages = 10;
      fixture.detectChanges();

      spyOn(component.previousButtonClick, 'emit');
      spyOn(component.nextButtonClick, 'emit');

      const nextButton = de.query(By.css('#nextButton'));
      nextButton.nativeElement.dispatchEvent(new Event('click'));
      fixture.detectChanges();

      expect(component.nextButtonClick.emit).toHaveBeenCalled();
      expect(component.previousButtonClick.emit).not.toHaveBeenCalled();
    });
  });
});
