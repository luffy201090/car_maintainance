import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMaintainanceDetails } from '../maintainance-details.model';
import { MaintainanceDetailsService } from '../service/maintainance-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './maintainance-details-delete-dialog.component.html',
})
export class MaintainanceDetailsDeleteDialogComponent {
  maintainanceDetails?: IMaintainanceDetails;

  constructor(protected maintainanceDetailsService: MaintainanceDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.maintainanceDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
