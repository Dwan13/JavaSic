import { createAction, props } from '@ngrx/store';

export const setUser = createAction(
  '[Auth] Set User',
  props<{ userId: string }>()
);

export const logout = createAction('[Auth] Logout');
