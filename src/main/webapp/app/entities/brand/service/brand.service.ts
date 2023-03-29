import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBrand, NewBrand } from '../brand.model';

export type PartialUpdateBrand = Partial<IBrand> & Pick<IBrand, 'id'>;

export type EntityResponseType = HttpResponse<IBrand>;
export type EntityArrayResponseType = HttpResponse<IBrand[]>;

@Injectable({ providedIn: 'root' })
export class BrandService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/brands');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(brand: NewBrand): Observable<EntityResponseType> {
    return this.http.post<IBrand>(this.resourceUrl, brand, { observe: 'response' });
  }

  update(brand: IBrand): Observable<EntityResponseType> {
    return this.http.put<IBrand>(`${this.resourceUrl}/${this.getBrandIdentifier(brand)}`, brand, { observe: 'response' });
  }

  partialUpdate(brand: PartialUpdateBrand): Observable<EntityResponseType> {
    return this.http.patch<IBrand>(`${this.resourceUrl}/${this.getBrandIdentifier(brand)}`, brand, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBrand>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBrand[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBrandIdentifier(brand: Pick<IBrand, 'id'>): number {
    return brand.id;
  }

  compareBrand(o1: Pick<IBrand, 'id'> | null, o2: Pick<IBrand, 'id'> | null): boolean {
    return o1 && o2 ? this.getBrandIdentifier(o1) === this.getBrandIdentifier(o2) : o1 === o2;
  }

  addBrandToCollectionIfMissing<Type extends Pick<IBrand, 'id'>>(
    brandCollection: Type[],
    ...brandsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const brands: Type[] = brandsToCheck.filter(isPresent);
    if (brands.length > 0) {
      const brandCollectionIdentifiers = brandCollection.map(brandItem => this.getBrandIdentifier(brandItem)!);
      const brandsToAdd = brands.filter(brandItem => {
        const brandIdentifier = this.getBrandIdentifier(brandItem);
        if (brandCollectionIdentifiers.includes(brandIdentifier)) {
          return false;
        }
        brandCollectionIdentifiers.push(brandIdentifier);
        return true;
      });
      return [...brandsToAdd, ...brandCollection];
    }
    return brandCollection;
  }
}
