import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaintainance } from '../maintainance.model';
import { MaintainanceService } from '../service/maintainance.service';

@Injectable({ providedIn: 'root' })
export class MaintainanceRoutingResolveService implements Resolve<IMaintainance | null> {
  constructor(protected service: MaintainanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMaintainance | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((maintainance: HttpResponse<IMaintainance>) => {
          if (maintainance.body) {
            return of(maintainance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
