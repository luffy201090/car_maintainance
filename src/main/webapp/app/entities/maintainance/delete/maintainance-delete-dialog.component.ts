import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMaintainance } from '../maintainance.model';
import { MaintainanceService } from '../service/maintainance.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './maintainance-delete-dialog.component.html',
})
export class MaintainanceDeleteDialogComponent {
  maintainance?: IMaintainance;

  constructor(protected maintainanceService: MaintainanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.maintainanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
