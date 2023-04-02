import Component from '~/views/component-search/Component';
import {Rating, RatingChangeEvent} from 'primereact/rating';
import React, {useEffect, useState} from 'react';
import {Image} from 'primereact/image';
import {Chip} from 'primereact/chip';
import {createReview, removeReview} from './reviews/ReviewService';
import useAuthContext from '../../context/AuthContext';
import {AxiosError} from 'axios';
import BasicResponse from '~/common/messages/BasicResponse';
import useToastContext from '../../context/ToastContext';
import {apiToToast} from '../../common/messages/messageHelper';

interface ComponentTemplateProps {
  component: Component;
  button: JSX.Element;
  additionalData?: JSX.Element | JSX.Element[];
  hideReviews?: boolean;
}

const ComponentTemplate = (props: ComponentTemplateProps) => {
  const product = props.component;
  const [rating, setRating] = useState(product.rating);
  const [ratingRounded, setRatingRounded] =
    useState(product.rating == undefined ? undefined : Math.round(product.rating));
  const [isReviewed, setIsReviewed] = useState(product.reviewed);
  const [reviewCount, setReviewCount] = useState(product.reviewCount);
  const {auth} = useAuthContext();
  const {toast} = useToastContext();

  const max = 5;

  useEffect(() => {
    setRating(product.rating);
    setIsReviewed(product.reviewed);
    setReviewCount(product.reviewCount);
  }, [product]);

  useEffect(() => {
    setRatingRounded(rating == undefined ? undefined : Math.round(rating));
  }, [rating]);

  const readOnly = !auth.authenticated;

  const setRatingIntercept = (event: RatingChangeEvent) => {
    event.preventDefault();

    if (event.value === null) {
      void runRemoveReview();
      return;
    }

    if (event.value === undefined) {
      return;
    }

    void runCreateReview(event.value);
  };

  const handleRequestFailure = (error: AxiosError<BasicResponse>) => {
    if (!error.response?.data?.messages?.length) {
      return;
    }
    toast.current?.show(error.response.data.messages.map(apiToToast));
  };

  const runCreateReview = async (rating: number) => {
    const res = await createReview({rating: rating, componentId: product.id}).catch(handleRequestFailure);
    if (!res) {
      return;
    }
    setRating(res.newRating);
    setReviewCount(res.newReviewCount);
    setIsReviewed(true);
  };

  const runRemoveReview = async () => {
    const res = await removeReview(product.id).catch(handleRequestFailure);
    if (!res) {
      return;
    }
    setRating(res.newRating);
    setReviewCount(res.newReviewCount);
    setIsReviewed(false);
  };

  return (
    <div className="col-12 md:col-12 lg:col-12 xl:col-12 sm:col-12">
      <div className="flex flex-column sm:flex-column md:flex-row xl:flex-row xl:align-items-start p-3 gap-4">
        <Image className="w-9 sm:w-16rem xl:w-10rem shadow-2 block xl:block mx-auto border-round pios-image"
          src={product.imageBase64 ? `data:image/jpeg;base64,${product.imageBase64}` : `/unknown.jpg`}
          alt={product.name} preview/>
        <div className="flex flex-column sm:flex-row justify-content-between align-items-center xl:align-items-start flex-1 gap-4">
          <div className="flex flex-column align-items-center sm:align-items-start gap-3">
            <div className="text-2xl font-bold text-900">{product.name}</div>
            {!props.hideReviews && <div className="flex align-items-start">
              <Rating className="mr-2" value={ratingRounded} onChange={setRatingIntercept} disabled={readOnly} max={max}
                tooltip={readOnly ? 'You need to log in to review components' : ''} cancel={auth.authenticated && isReviewed}
                tooltipOptions={{position: 'top', showOnDisabled: true}}/>
              <span>{rating == undefined ? '' : `${rating}/${max}`} ({reviewCount} review{reviewCount == 1 ? '' : 's'})</span>
            </div>}
            <div className="flex">
              <Chip label={product.manufacturer.name} className="mr-2"/>
              <div className="flex align-items-center gap-3">
                <span className="flex align-items-center gap-2">
                  <i className="pi pi-tag"/>
                  <span className="font-semibold">{product.type}</span>
                </span>
              </div>
            </div>
            {props.additionalData}
          </div>
          <div className="flex flex-column sm:flex-column align-items-center sm:align-items-end gap-3 sm:gap-2">
            <span className="text-2xl font-semibold w-full text-center">{product.price}€</span>
            {props.button}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ComponentTemplate;

