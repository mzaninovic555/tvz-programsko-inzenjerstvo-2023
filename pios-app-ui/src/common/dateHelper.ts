export const formatDate = (date?: Date) => {
  if (!date) {
    return 'N/A';
  }
  return `${date.getFullYear()}-${normalize(date.getMonth() + 1)}-${normalize(date.getDate())}`;
};

export const normalize = (num: number): string => {
  return num.toLocaleString('de', {minimumIntegerDigits: 2});
};
