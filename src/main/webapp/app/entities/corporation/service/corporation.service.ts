import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICorporation, NewCorporation } from '../corporation.model';

export type PartialUpdateCorporation = Partial<ICorporation> & Pick<ICorporation, 'id'>;

export type EntityResponseType = HttpResponse<ICorporation>;
export type EntityArrayResponseType = HttpResponse<ICorporation[]>;

@Injectable({ providedIn: 'root' })
export class CorporationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/corporations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(corporation: NewCorporation): Observable<EntityResponseType> {
    return this.http.post<ICorporation>(this.resourceUrl, corporation, { observe: 'response' });
  }

  update(corporation: ICorporation): Observable<EntityResponseType> {
    return this.http.put<ICorporation>(`${this.resourceUrl}/${this.getCorporationIdentifier(corporation)}`, corporation, {
      observe: 'response',
    });
  }

  partialUpdate(corporation: PartialUpdateCorporation): Observable<EntityResponseType> {
    return this.http.patch<ICorporation>(`${this.resourceUrl}/${this.getCorporationIdentifier(corporation)}`, corporation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICorporation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICorporation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCorporationIdentifier(corporation: Pick<ICorporation, 'id'>): number {
    return corporation.id;
  }

  compareCorporation(o1: Pick<ICorporation, 'id'> | null, o2: Pick<ICorporation, 'id'> | null): boolean {
    return o1 && o2 ? this.getCorporationIdentifier(o1) === this.getCorporationIdentifier(o2) : o1 === o2;
  }

  addCorporationToCollectionIfMissing<Type extends Pick<ICorporation, 'id'>>(
    corporationCollection: Type[],
    ...corporationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const corporations: Type[] = corporationsToCheck.filter(isPresent);
    if (corporations.length > 0) {
      const corporationCollectionIdentifiers = corporationCollection.map(
        corporationItem => this.getCorporationIdentifier(corporationItem)!
      );
      const corporationsToAdd = corporations.filter(corporationItem => {
        const corporationIdentifier = this.getCorporationIdentifier(corporationItem);
        if (corporationCollectionIdentifiers.includes(corporationIdentifier)) {
          return false;
        }
        corporationCollectionIdentifiers.push(corporationIdentifier);
        return true;
      });
      return [...corporationsToAdd, ...corporationCollection];
    }
    return corporationCollection;
  }
}
