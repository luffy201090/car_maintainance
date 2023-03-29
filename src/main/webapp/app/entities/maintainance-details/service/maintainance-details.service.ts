import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaintainanceDetails, NewMaintainanceDetails } from '../maintainance-details.model';

export type PartialUpdateMaintainanceDetails = Partial<IMaintainanceDetails> & Pick<IMaintainanceDetails, 'id'>;

export type EntityResponseType = HttpResponse<IMaintainanceDetails>;
export type EntityArrayResponseType = HttpResponse<IMaintainanceDetails[]>;

@Injectable({ providedIn: 'root' })
export class MaintainanceDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/maintainance-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(maintainanceDetails: NewMaintainanceDetails): Observable<EntityResponseType> {
    return this.http.post<IMaintainanceDetails>(this.resourceUrl, maintainanceDetails, { observe: 'response' });
  }

  update(maintainanceDetails: IMaintainanceDetails): Observable<EntityResponseType> {
    return this.http.put<IMaintainanceDetails>(
      `${this.resourceUrl}/${this.getMaintainanceDetailsIdentifier(maintainanceDetails)}`,
      maintainanceDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(maintainanceDetails: PartialUpdateMaintainanceDetails): Observable<EntityResponseType> {
    return this.http.patch<IMaintainanceDetails>(
      `${this.resourceUrl}/${this.getMaintainanceDetailsIdentifier(maintainanceDetails)}`,
      maintainanceDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMaintainanceDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMaintainanceDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaintainanceDetailsIdentifier(maintainanceDetails: Pick<IMaintainanceDetails, 'id'>): number {
    return maintainanceDetails.id;
  }

  compareMaintainanceDetails(o1: Pick<IMaintainanceDetails, 'id'> | null, o2: Pick<IMaintainanceDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaintainanceDetailsIdentifier(o1) === this.getMaintainanceDetailsIdentifier(o2) : o1 === o2;
  }

  addMaintainanceDetailsToCollectionIfMissing<Type extends Pick<IMaintainanceDetails, 'id'>>(
    maintainanceDetailsCollection: Type[],
    ...maintainanceDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const maintainanceDetails: Type[] = maintainanceDetailsToCheck.filter(isPresent);
    if (maintainanceDetails.length > 0) {
      const maintainanceDetailsCollectionIdentifiers = maintainanceDetailsCollection.map(
        maintainanceDetailsItem => this.getMaintainanceDetailsIdentifier(maintainanceDetailsItem)!
      );
      const maintainanceDetailsToAdd = maintainanceDetails.filter(maintainanceDetailsItem => {
        const maintainanceDetailsIdentifier = this.getMaintainanceDetailsIdentifier(maintainanceDetailsItem);
        if (maintainanceDetailsCollectionIdentifiers.includes(maintainanceDetailsIdentifier)) {
          return false;
        }
        maintainanceDetailsCollectionIdentifiers.push(maintainanceDetailsIdentifier);
        return true;
      });
      return [...maintainanceDetailsToAdd, ...maintainanceDetailsCollection];
    }
    return maintainanceDetailsCollection;
  }
}
