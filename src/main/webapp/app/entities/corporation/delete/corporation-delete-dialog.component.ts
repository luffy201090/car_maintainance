import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICorporation } from '../corporation.model';
import { CorporationService } from '../service/corporation.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './corporation-delete-dialog.component.html',
})
export class CorporationDeleteDialogComponent {
  corporation?: ICorporation;

  constructor(protected corporationService: CorporationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.corporationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
