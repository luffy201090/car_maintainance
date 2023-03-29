export interface ICorporation {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewCorporation = Omit<ICorporation, 'id'> & { id: null };
