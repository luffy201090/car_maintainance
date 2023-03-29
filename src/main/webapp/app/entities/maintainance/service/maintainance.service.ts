import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMaintainance, NewMaintainance } from '../maintainance.model';

export type PartialUpdateMaintainance = Partial<IMaintainance> & Pick<IMaintainance, 'id'>;

type RestOf<T extends IMaintainance | NewMaintainance> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestMaintainance = RestOf<IMaintainance>;

export type NewRestMaintainance = RestOf<NewMaintainance>;

export type PartialUpdateRestMaintainance = RestOf<PartialUpdateMaintainance>;

export type EntityResponseType = HttpResponse<IMaintainance>;
export type EntityArrayResponseType = HttpResponse<IMaintainance[]>;

@Injectable({ providedIn: 'root' })
export class MaintainanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/maintainances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(maintainance: NewMaintainance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintainance);
    return this.http
      .post<RestMaintainance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(maintainance: IMaintainance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintainance);
    return this.http
      .put<RestMaintainance>(`${this.resourceUrl}/${this.getMaintainanceIdentifier(maintainance)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(maintainance: PartialUpdateMaintainance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(maintainance);
    return this.http
      .patch<RestMaintainance>(`${this.resourceUrl}/${this.getMaintainanceIdentifier(maintainance)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMaintainance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMaintainance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaintainanceIdentifier(maintainance: Pick<IMaintainance, 'id'>): number {
    return maintainance.id;
  }

  compareMaintainance(o1: Pick<IMaintainance, 'id'> | null, o2: Pick<IMaintainance, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaintainanceIdentifier(o1) === this.getMaintainanceIdentifier(o2) : o1 === o2;
  }

  addMaintainanceToCollectionIfMissing<Type extends Pick<IMaintainance, 'id'>>(
    maintainanceCollection: Type[],
    ...maintainancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const maintainances: Type[] = maintainancesToCheck.filter(isPresent);
    if (maintainances.length > 0) {
      const maintainanceCollectionIdentifiers = maintainanceCollection.map(
        maintainanceItem => this.getMaintainanceIdentifier(maintainanceItem)!
      );
      const maintainancesToAdd = maintainances.filter(maintainanceItem => {
        const maintainanceIdentifier = this.getMaintainanceIdentifier(maintainanceItem);
        if (maintainanceCollectionIdentifiers.includes(maintainanceIdentifier)) {
          return false;
        }
        maintainanceCollectionIdentifiers.push(maintainanceIdentifier);
        return true;
      });
      return [...maintainancesToAdd, ...maintainanceCollection];
    }
    return maintainanceCollection;
  }

  protected convertDateFromClient<T extends IMaintainance | NewMaintainance | PartialUpdateMaintainance>(maintainance: T): RestOf<T> {
    return {
      ...maintainance,
      date: maintainance.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMaintainance: RestMaintainance): IMaintainance {
    return {
      ...restMaintainance,
      date: restMaintainance.date ? dayjs(restMaintainance.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMaintainance>): HttpResponse<IMaintainance> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMaintainance[]>): HttpResponse<IMaintainance[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
