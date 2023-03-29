import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICorporation } from '../corporation.model';
import { CorporationService } from '../service/corporation.service';

@Injectable({ providedIn: 'root' })
export class CorporationRoutingResolveService implements Resolve<ICorporation | null> {
  constructor(protected service: CorporationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICorporation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((corporation: HttpResponse<ICorporation>) => {
          if (corporation.body) {
            return of(corporation.body);
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
