import ComponentResponse from '~/views/component-search/ComponentResponse';

interface WishlistEntry {
  id: number;
  component: ComponentResponse;
  addedAt: Date;
}

export default WishlistEntry;
